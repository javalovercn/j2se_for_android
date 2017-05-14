package hc.android.loader;

import hc.android.ActivityManager;
import hc.server.PlatformManager;
import hc.util.ClassUtil;
import hc.util.ResourceUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import dalvik.system.DexClassLoader;

public class AndroidDX {
	private static final File tempWriteDir = new File(ActivityManager.getActivity().getFilesDir(), "_dx_tmp_");
	
	static{
		if(tempWriteDir.exists() == false){
			tempWriteDir.mkdirs();
			System.out.println("create dir : " + tempWriteDir.getAbsolutePath());
		}
		ResourceUtil.clearDir(tempWriteDir);
	}

	public static boolean dx(File srcFile, File outputFileName) throws Exception{
		File target = new File(tempWriteDir, ResourceUtil.createRandomFileNameWithExt(tempWriteDir, ResourceUtil.EXT_JAR));
		try{
			copyJarWithoutMetaInf(srcFile, target);
			return dxWithoutMetaInfo(target, outputFileName);
		}finally{
			target.delete();
		}
	}
	
	/**
	 * @param srcFile
	 * @param outputFileName
	 * @return
	 * @throws Exception
	 */
	private static boolean dxWithoutMetaInfo(File srcFile, File outputFileName) throws Exception{
		File baseDir = PlatformManager.getService().getBaseDir();
		
		//重要：dx.dex在Starter也出现，请注意同步
		File dex = new File(baseDir, "dx.dex");//如果此包发生升级，由HCAndroidStarter负责维持升级及optDir
		if(dex.canRead() == false){
			throw new Error("dx.dex is NOT exists!");
		}
		
		String dexerMain = "com.android.dx.command.dexer.Main";

		String absolutePath = dex.getAbsolutePath();
		
		//File.pathSeparator
		File dxCacheFile = new File(baseDir, "dxCache");
		dxCacheFile.mkdirs();//因为升级SDK有可能导致这些目录被删除，所以要重建
		DexClassLoader dexFileClassLoader = new DexClassLoader(absolutePath, dxCacheFile.getAbsolutePath(), null, ActivityManager
				.getActivity().getClassLoader());
		Class cd = dexFileClassLoader.loadClass(dexerMain);
		
		Class[] paraTypes = {String[].class};
		
//		测试通过
//		String[] dexparas = {"--core-library", "--output=" + outputFileName.getAbsolutePath(), srcFile.getAbsolutePath()};
		
		String[] dexparas = {"--output=" + outputFileName.getAbsolutePath(), srcFile.getAbsolutePath()};
		Object[] paras = {dexparas};
		//--dex --output=dexed.jar hello.jar
		outputFileName.delete();
		
		ClassUtil.invokeWithExceptionOut(cd, cd, "main", paraTypes, paras, false);
		
		if(outputFileName.exists()){
			return true;
		}
		return false;
	}
	
	public static boolean copyJarWithoutMetaInf(File src, File target) {
		JarOutputStream jos = null;
		InputStream jis = null;
		try{
			jos = new JarOutputStream(new FileOutputStream(target));
			JarFile srcJarFile = new JarFile(src);
			byte[] buffers = new byte[2048];
			Enumeration<JarEntry> entrys = srcJarFile.entries();
			int len = 0;
			while (entrys.hasMoreElements()) {  
				JarEntry inEntry = entrys.nextElement();  
				
				if(inEntry.isDirectory() || inEntry.getName().startsWith("META-INF")){
					continue;
				}
				
				jis = srcJarFile.getInputStream(inEntry);
				jos.putNextEntry(inEntry);
				while((len = jis.read(buffers)) >= 0){
					jos.write(buffers, 0, len);
				}
				jis.close();
				jos.closeEntry();
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				jos.close();
			}catch (Exception e) {
			}
			try{
				jis.close();
			}catch (Exception e) {
			}
		}
		return false;
	}
}
