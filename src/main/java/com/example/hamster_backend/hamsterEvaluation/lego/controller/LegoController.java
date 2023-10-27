package com.example.hamster_backend.hamsterEvaluation.lego.controller;

//import at.ac.htlinn.hamsterProgram..hamsterEvaluation.lego.view.Browser;
import com.example.hamster_backend.hamsterEvaluation.model.HamsterFile;
import com.example.hamster_backend.hamsterEvaluation.lego.model.LegoModel;
import com.example.hamster_backend.hamsterEvaluation.workbench.Workbench;

/**
 * Dies ist der Controller-Teil der Lego-Komponente.
 * 
 * @author Christian
 */
public class LegoController {
	private Workbench workbench;
	private LegoModel model;
	private HamsterFile activeFile;

	/**
	 * Dieser ActionCommand wird von einer Action benutzt, die das Hochladen einer
	 * Lego-Datei startet.
	 */
	public static final String ACTION_UPLOAD = "upload";

	/**
	 * Der Konstruktor des LegoControllers. Er erzeugt die View-Komponenten.
	 * 
	 * @param model     Das schon erzeugte Model
	 * @param workbench Die Werkbank
	 */
	public LegoController(LegoModel model, Workbench workbench) {
		this.workbench = workbench;
		this.model = model;
	}

	public Workbench getWorkbench() {
		return workbench;
	}

	/*
	 * Setzt den neuen Wert von activeFile und �bergibt diesen auch gleich der
	 * Viewkomponente.
	 * 
	 * @param activeFile Der neue Wert von activeFile.
	 */
	public void setActiveFile(HamsterFile file) {
		activeFile = file;
	}
}
