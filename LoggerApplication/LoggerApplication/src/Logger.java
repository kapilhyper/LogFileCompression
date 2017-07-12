// testing github
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Logger {

	public static void main(String[] args) {

		Logger logger = new Logger();
		FileInputStream fis = null;
		Properties props = new Properties();
		String filepath = "E:" + File.separator + "HyperCity" + File.separator + "LoggerDesktopApplication"
				+ File.separator + "property.properties";
		try {
			fis = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (fis != null) {
			try {
				props.load(fis);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// System.out.println(props.getProperty("folder_path"));
			String[] array = props.getProperty("folder_path").split(",");
			int days = Integer.parseInt(props.getProperty("number_of_days"));
			System.out.println("DAYS ::: "+days);

			for (String folderPath : array) {
				System.out.println("Folder Path :::: "+folderPath);

				File folder = new File(folderPath);
				File[] listOfFiles = folder.listFiles();
				String filename;

				int cnt = 1;
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						filename = listOfFiles[i].getName(); 
						System.out.println(cnt+") File ::: " + filename);
						
						String extension = filename.substring(filename.lastIndexOf("."));

						/*int j = filename.lastIndexOf('.');
						if (j > 0) {
							extension = filename.substring(j + 1);
						}*/
						
						if(!extension.equals(".zip"))
						{
							logger.zipIt(filename, folderPath);
						}
						else{
							logger.deleteZip(filename, folderPath,days);
						}
					} else if (listOfFiles[i].isDirectory()) {
						 System.out.println("Directory ::::: " + listOfFiles[i].getName());
					}
					cnt++;
				}
			}

		}

	}

	public void zipIt(String filename, String sourceFolder) {

		System.out.println("In zip file............");
		//System.out.println("FileName :: " + filename);
		//System.out.println("FolderPath :: " + sourceFolder);

		String file = sourceFolder + File.separator + filename;
		System.out.println("File :: " + file);
		String path = sourceFolder + File.separator + filename.substring(0, filename.lastIndexOf("."));
		//System.out.println(path);

		byte[] buffer = new byte[1024];

		try {

			FileOutputStream fos = new FileOutputStream(path + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			ZipEntry ze = new ZipEntry(filename);
			zos.putNextEntry(ze);
			FileInputStream in = new FileInputStream(file);

			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

			in.close();
			zos.closeEntry();

			// remember close it
			zos.close();

			System.out.println("Done");
			File f = new File(file);
			
			
				f.delete();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void deleteZip(String filename, String sourceFolder,int days) {

		System.out.println("In delete file *************************************");
		
		String file = sourceFolder + File.separator + filename;

		File f = new File(file);

		
		//System.out.println("Last Modified ::: " + f.lastModified());

		long eligibleForDeletion = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000);
		
		
		if (f.lastModified() < eligibleForDeletion ) {
			System.out.println("--------------++++++++++++++++++++++_______________");
			f.delete();
		}
	}
}
