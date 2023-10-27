package com.example.hamster_backend.hamsterEvaluation.model;

import java.io.Serializable;

import com.example.hamster_backend.hamsterEvaluation.debugger.model.Hamster;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class HamsterNotInitializedException extends
		HamsterNichtInitialisiertException implements Serializable {
	public HamsterNotInitializedException(Hamster hamster) {
		super(hamster);
	}

	public HamsterNotInitializedException(HamsterNichtInitialisiertException e) {
		super(e.hamster);
	}

}