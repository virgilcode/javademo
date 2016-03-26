/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virgil.design.observer.demo1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author virgil 抽象主题类
 */
public abstract class Subject {

    private List<Observer> observers = new ArrayList<Observer>();

    //增加观察者
    public void attach(Observer observer) {
        this.observers.add(observer);
    }

    //删除观察者
    public void detach(Observer observer) {
        this.observers.remove(observer);
    }

    //发出通知
    public void Notify() {
        for(int i=0;i<this.observers.size();i++){
            observers.get(i).Update();
        }
    }
}

