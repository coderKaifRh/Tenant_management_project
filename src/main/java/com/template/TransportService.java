package com.template;
public class TransportService extends TAKService {
    private String size; private int workers;
    public TransportService(String size, int workers) { this.size = size; this.workers = workers; }

    public double calculateTotalCost() throws TAKException {
        if(workers < 0) throw new TAKException("Workers cannot be negative!");
        double base = size.equals("Large") ? 5000 : size.equals("Medium") ? 3500 : 2000;
        return base + (workers * 800);
    }
}