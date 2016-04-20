/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadpool.demo1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author virgil
 */
public class TaskCallDemo {

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        List<Future<String>> res = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            res.add(exec.submit(new TaskWithResult(i)));
        }
        for (Future<String> f : res) {
            try {
                if (f.isDone()) {
                    System.out.println(f.get());
                };
            } catch (InterruptedException ex) {
                Logger.getLogger(TaskCallDemo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(TaskCallDemo.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                exec.shutdown();
            }
        }
    }
}
