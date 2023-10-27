package com.example.hamster_backend.hamsterEvaluation.model;

import java.io.Serializable;

import com.example.hamster_backend.hamsterEvaluation.debugger.model.Hamster;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class MaulLeerException extends HamsterException implements Serializable {
	public MaulLeerException(Hamster hamster) {
		super(hamster, "1");
	}

	public String toString() {
		return "hamster.MaulLeerException";
	}
}