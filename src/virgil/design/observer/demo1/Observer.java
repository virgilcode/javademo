/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package virgil.design.observer.demo1;

/**
 *
 * @author virgil
 * 抽象观察者，为所有具体观察者定义一个接口，在得到通知时更新自己
 */
public abstract class Observer {
    public abstract void Update();
}
