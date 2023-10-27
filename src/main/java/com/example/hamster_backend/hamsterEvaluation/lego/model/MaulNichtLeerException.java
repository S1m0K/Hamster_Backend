package com.example.hamster_backend.hamsterEvaluation.lego.model;


import com.example.hamster_backend.hamsterEvaluation.model.HamsterException;
import com.example.hamster_backend.hamsterEvaluation.debugger.model.Hamster;
import java.io.Serializable;

/**
 *
 * @author Christian
 */
public class MaulNichtLeerException extends HamsterException implements Serializable {
        
        protected Hamster hamster;
        
        public MaulNichtLeerException(Hamster hamster) {
                super(hamster, "6");
                this.hamster = hamster;
                
        }
        
        public String toString() {
                return "hamster.MaulNichtLeerException";
        }
        
        
        
        
}

