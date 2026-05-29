package model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReservationRepository {
    private ArrayList<Reservation> reservations;
    private final String FILE_PATH = "reservations.txt";

    public ReservationRepository() {
        this.reservations = new ArrayList<>();
        loadFromFile();
    }

    public synchronized void addReservation(Reservation r) {
        this.reservations.add(r);
        saveToFile();
    }

    public ArrayList<Reservation> getAllReservations() {
        return reservations;
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Reservation r : reservations) {
                // 좌석 ID들을 "/"로 구분해서 저장 (예: C4/C5)
                StringBuilder seatIds = new StringBuilder();
                for (int i = 0; i < r.getSeats().size(); i++) {
                    if (i > 0) seatIds.append("/");
                    seatIds.append(r.getSeats().get(i).getSeatId());
                }

                bw.write(r.getReservationNumber() + "," +
                         r.getShowInfo().getMovie().getTitle() + "," +
                         r.getShowInfo().getTime() + "," +
                         r.getTotalPrice() + "," +
                         r.getBookingFee() + "," +
                         seatIds + "," +
                         r.getStatus());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 7) {
                    String resNum   = data[0];
                    String movieTitle = data[1];
                    String time     = data[2];
                    int price       = Integer.parseInt(data[3]);
                    int fee         = Integer.parseInt(data[4]);
                    String seatData = data[5];
                    String status   = data[6];

                    // 좌석 복원: "C4/C5" → Seat 리스트
                    ArrayList<Seat> seats = new ArrayList<>();
                    if (!seatData.isEmpty()) {
                        for (String seatId : seatData.split("/")) {
                            seats.add(new Seat(seatId));
                        }
                    }

                    Movie tempMovie = new Movie(movieTitle, null, 0, 0, null);
                    ShowInfo tempInfo = new ShowInfo(tempMovie, null, time, 0);
                    Reservation r = new Reservation(resNum, tempInfo, seats, price, fee);

                    if (status.equals("취소")) {
                        r.cancelReservation();
                    }

                    this.reservations.add(r);
                }
            }
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류 발생: " + e.getMessage());
        }
    }
}