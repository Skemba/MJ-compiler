package rs.ac.bg.etf.ma120261d_pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java_cup.runtime.Symbol;

public class MJTest {
	private static final Logger logger = LogManager.getLogger(MJParserTest.class);
	public static void main(String[] args) throws IOException {
		Reader br = null;
		boolean stdout = true;
		Path outFile = Paths.get("lekser.txt");
		File sourceCode = new File("test/test1.mj");
		if(Files.exists(outFile)) {
			Files.delete(outFile);
		}
		
		if(args.length < 1) {
			logger.error("Nedovoljno argumenata");
			return;
		} else {
			if(args[0].equals("stdout")) {
				stdout = true;
				sourceCode = new File(args[1]);
			} else if(args[0].equals("file")){
				if(args.length == 3) {
					outFile = Paths.get(args[1]);
					sourceCode = new File(args[2]);
					stdout = false;
				} else if(args.length == 2) {
					stdout = false;
				} else {
					logger.error("Los broj argumenata");
					return;
				}
			} else {
				logger.error("Previse argumenata");
				return;
			}
			
			
			try {
				logger.info("Compiling source file: " + sourceCode.getAbsolutePath());
				
				br = new BufferedReader(new FileReader(sourceCode));
				
				Yylex lexer = new Yylex(br);
				Symbol currToken = null;
				while ((currToken = lexer.next_token()).sym != sym.EOF) {
					if (currToken != null) {
						if(stdout) {
							System.out.println(currToken.toString() + " " + currToken.value.toString());
						} else {
							List<String> token = Arrays.asList(currToken.toString() + " " + currToken.value.toString());
							try {
								Files.write(outFile, token, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
							} catch (NoSuchFileException e) {
								Files.write(outFile, token, Charset.forName("UTF-8"));
							}
						}
					}
				}
			} 
			finally {
				if (br != null) try { br.close(); } catch (IOException e1) { e1.printStackTrace(); }
			}
		}
	}
	
}
