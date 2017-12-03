package rs.ac.bg.etf.ma120261d_pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusLogger;

import java.io.Reader;

import java_cup.runtime.Symbol;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class MJParserTest {
	
	private static final Logger logger = LogManager.getLogger(MJParserTest.class);
	public static void main(String[] args) throws Exception {
	    StatusConsoleListener listener = new StatusConsoleListener(Level.ERROR);
	    StatusLogger.getLogger().registerListener(listener);
		String fileName; 
		
		Reader br = null;
		if(args.length > 4) {
			logger.error("There should be exactly one command line argument!");
		} else {
			fileName = args[0];
			if(fileName.lastIndexOf(".") == -1 || !fileName.substring(fileName.lastIndexOf(".")).equals(".mj")) {
				logger.error("Wrong file name!");
			} else {
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			
				try {
					File sourceCode = new File(fileName + ".mj");
					logger.info("Compiling source file: " + sourceCode.getAbsolutePath());
					
					br = new BufferedReader(new FileReader(sourceCode));
					Yylex lexer = new Yylex(br);
					
					MJParser p = new MJParser(lexer);
			        Symbol s = p.parse();  //pocetak parsiranja
			        
			        logger.debug("=====================SYNTAX ANALYSIS=========================");
			        
			        logger.info("Print calls = " + p.printCallCount);
			        logger.info("Global constants definitions = " + p.globalConstsDefCount);
			        logger.info("Global variable definitions = " + p.globalVarsDefCount);
			        logger.info("Local variable definitions in main function = " + p.localVarsDefCount);
			        logger.info("Global array variable declarations = " + p.globalVarsArrDeclCount);
			        
			        
			        Tab.dump();
			        
			        if(!p.errorDetected) {
			        	File objFile = new File(fileName + ".obj");
			        	if(objFile.exists()) {
			        		objFile.delete();
			        	}
			        	Code.write(new FileOutputStream(fileName + ".obj"));
			        	logger.info("Parsiranje uspesno!");
			        } else {
			        	logger.error("Error : Parsiranje neuspesno!");
			        }
			        
				} 
				finally {
					if (br != null) try { br.close(); } catch (IOException e1) { logger.error(e1.getMessage()); }
				}
			}
		}

	}
	
}
