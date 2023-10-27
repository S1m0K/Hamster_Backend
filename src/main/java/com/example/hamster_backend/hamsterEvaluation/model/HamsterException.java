package com.example.hamster_backend.hamsterEvaluation.model;

import java.io.Serializable;

import com.example.hamster_backend.hamsterEvaluation.debugger.model.Hamster;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class HamsterException extends RuntimeException implements Serializable {
	protected Hamster hamster;
	protected String errorCode; 

	public HamsterException(Hamster hamster, String errorCode) {
		this.hamster = hamster;
		this.errorCode = errorCode; 
		System.out.println(errorCode);
	}

	public void setHamster(Hamster hamster) {
		this.hamster = hamster;
	}

	public Hamster getHamster() {
		return hamster;
	}
}