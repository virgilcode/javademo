/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virgil.design.observer.demo1;

/**
 *
 * @author virgil
 */
public class ConcreteObserver extends Observer {

    private String observerstate;
    private String name;
    private ConcreteSubject subject;

    public ConcreteSubject getSubject() {
        return subject;
    }

    public void setSubject(ConcreteSubject subject) {
        this.subject = subject;
    }

    public ConcreteObserver(String name, ConcreteSubject subject) {
        this.name = name;
        this.subject = subject;
    }

    

    @Override
    public void Update() {
        this.observerstate=this.subject.getSubjectState();
        System.out.println("the observer's state of "+name+" is "+observerstate);
    }

}
