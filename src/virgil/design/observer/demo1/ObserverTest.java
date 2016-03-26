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
public class ObserverTest {
    public static void main(String[] args) {
        ConcreteSubject subject=new ConcreteSubject();
        subject.attach(new ConcreteObserver("Observer A", subject));
        subject.attach(new ConcreteObserver("Observer B", subject));
        subject.attach(new ConcreteObserver("Observer C", subject));
        subject.setSubjectState("ready");
        subject.Notify();
    }
}
