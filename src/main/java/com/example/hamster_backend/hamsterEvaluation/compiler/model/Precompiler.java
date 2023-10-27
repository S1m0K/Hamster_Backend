package com.example.hamster_backend.hamsterEvaluation.compiler.model;


import com.example.hamster_backend.hamsterEvaluation.model.HamsterFile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class Precompiler {
	String text;
	int pos;

	PrintWriter out;

	public Precompiler() {
	}

	public JavaToken nextToken(HamsterLexer lexer) throws IOException {
		JavaToken t = lexer.nextToken();
		while (t != null && (t.isComment() | t.isWhiteSpace()))
			t = lexer.nextToken();
		return t;
	}

	public boolean precompile(HamsterFile file) throws IOException {
		String text = file.load();
		char type = file.getType();
		String className = file.getName();
		String javaName = file.getAbsoluteJava();
		if (javaName == null) {
			return false;
		}
		PrintWriter out = new PrintWriter(new FileWriter(javaName));
		HamsterLexer lexer = new HamsterLexer();
		lexer.init(0, 0, text);
		int pos = 0;

		JavaToken t = nextToken(lexer);

		// package
		if (type == HamsterFile.HAMSTERCLASS) {
			if (t.getText().equals("package")) {
				t = nextToken(lexer);
				while (t != null && !t.getText().equals(";"))
					t = nextToken(lexer);
				t = nextToken(lexer);
			}
		}

		// imports
		while (t != null && t.getText().equals("import")) {
			t = nextToken(lexer);
			while (t != null && !t.getText().equals(";"))
				t = nextToken(lexer);
			t = nextToken(lexer);
		}

		if (t != null) {
			out.print(text.substring(pos, t.getStart()));
			pos = t.getStart();
			out.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.debugger.com.example.hamster_backend.model.Territorium;");
			out.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.debugger.com.example.hamster_backend.model.Territory;");
			/* dibo 20.05.05 */out
					.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.HamsterException;");
			out
					.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.HamsterInitialisierungsException;");
			out
					.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.HamsterNichtInitialisiertException;");
			out.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.KachelLeerException;");
			out.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.MauerDaException;");
			out.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.MaulLeerException;");
			out.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.MouthEmptyException;");
			out.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.WallInFrontException;");
			out.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.TileEmptyException;");
			/* lego */if (type != HamsterFile.IMPERATIVE
					&& type != HamsterFile.LEGOPROGRAM)
				out.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.debugger.com.example.hamster_backend.model.Hamster;");
			if (type == HamsterFile.OBJECT) {
				out.print("public class " + className.substring(0, className.length() - 5)
						+ " implements at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.HamsterProgram {");
				System.out.println("Object");
			}else if (type == HamsterFile.IMPERATIVE)
				out.print("public class " + className
						+ " extends at.ac.htlinn.hamsterProgram..hamsterEvaluation.debugger.com.example.hamster_backend.model.IHamster"
						+ " implements at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.HamsterProgram {");
			/* Lego-Christian */else if (type == HamsterFile.LEGOPROGRAM) {
				out
						.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.lego.com.example.hamster_backend.model.MaulNichtLeerException;");
				out.print("import at.ac.htlinn.hamsterProgram..hamsterEvaluation.lego.com.example.hamster_backend.model.KornDaException;");
				out.print("public class " + className
						+ " extends at.ac.htlinn.hamsterProgram..hamsterEvaluation.lego.com.example.hamster_backend.model.LHamster"
						+ " implements at.ac.htlinn.hamsterProgram..hamsterEvaluation.com.example.hamster_backend.model.HamsterProgram {");
			}
		}

		while (t != null && !(type == HamsterFile.HAMSTERCLASS)) {
			if (t != null && t.getText().equals("void")) {
				int start = t.getStart();
				t = nextToken(lexer);
				if (t.getText() == null)
					break;
				if (t != null && t.getText().equals("main")) {
					t = nextToken(lexer);
					if (t.getText() == null)
						break;
					if (t != null && t.getText().equals("(")) {
						t = nextToken(lexer);
						if (t.getText() == null)
							break;
						if (t != null && t.getText().equals(")")) {
							out.print(text.substring(pos, start));
							pos = start;
							out.print("public ");
						}
					}
				}
			}
			t = nextToken(lexer);
		}

		out.print(text.substring(pos));
		if (type != HamsterFile.HAMSTERCLASS)
			out.print("}");
		out.close();
		return true;
	}
}
