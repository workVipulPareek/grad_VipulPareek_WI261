package com.example.interface_testing;

public class DummyI implements Interface {
	@Override
    public void process() {
        System.out.println("Processing...");
    }

    @Override
    public String getData() {
        return "Server Data";
    }

    @Override
    public void execute() {
        System.out.println("Executing...");
    }
}
