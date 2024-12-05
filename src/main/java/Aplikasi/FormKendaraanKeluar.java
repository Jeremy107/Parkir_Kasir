/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Aplikasi;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LENOVO
 */
public class FormKendaraanKeluar extends javax.swing.JFrame {

    /**
     * Creates new form FormKendaraanKeluar
     */
    private void tampilkan_data() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Nomor");
    model.addColumn("No Tiket");
    model.addColumn("Nomor Plat");
    model.addColumn("Jenis Kendaraan");
    model.addColumn("Tanggal Masuk");
    model.addColumn("Jam Masuk");
    model.addColumn("Jam Masuk");
    model.addColumn("Biaya");

    try {
        int no = 1;
        String sql = "SELECT * FROM parkirmasuk WHERE Biaya > 0";
        java.sql.Connection conn = (Connection) Config.configDB();
        java.sql.Statement stm = conn.createStatement();
        java.sql.ResultSet res = stm.executeQuery(sql);

        while (res.next()) {
            model.addRow(new Object[]{
                no++,
                res.getInt("notiket"),
                res.getString("noplat"),
                res.getString("jeniskendaraan"),
                res.getDate("tanggalmasuk"),
                res.getTime("jammasuk"),
                res.getTime("jam_keluar"),
                res.getInt("Biaya")
            });
        }

        tabelDataKeluar.setModel(model);
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
    
    private void searchData(String field, String value) {
     try {
        String sql = "SELECT * FROM parkirmasuk WHERE " + field + " = ?";
        java.sql.Connection conn = (Connection) Config.configDB();
        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, value);
        java.sql.ResultSet res = pst.executeQuery();

        if (res.next()) {
            // Mengisi nilai-nilai ke dalam JTextField
            txtTiketKeluar.setText(res.getString("notiket"));
            txtNoPlatKeluar.setText(res.getString("noplat"));
            txtJenisKeluar.setText(res.getString("jeniskendaraan"));
            txtTglMasuk.setText(res.getString("tanggalmasuk"));
            txtJamMasuk.setText(res.getString("jammasuk"));

            // Mendapatkan waktu saat tombol cari diklik dan menyimpannya di database
            java.util.Date date = new java.util.Date();
            java.sql.Time time = new java.sql.Time(date.getTime());

            // Menyimpan waktu keluar di database
            String updateQuery = "UPDATE parkirmasuk SET jam_keluar = ? WHERE " + field + " = ?";
            java.sql.PreparedStatement updatePst = conn.prepareStatement(updateQuery);
            updatePst.setTime(1, time);
            updatePst.setString(2, value);
            updatePst.executeUpdate();

            // Menampilkan waktu keluar di JTextField
            txtJamKeluar.setText(time.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan tolong isi No tiket", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
         private void calculateParkingFee() {
        try {
            String tiketNumber = txtTiketKeluar.getText();
            String query = "SELECT * FROM parkirmasuk WHERE notiket = ?";
            Connection conn = Config.configDB();
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, tiketNumber);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Mengambil waktu masuk dan waktu keluar
                Time jamMasuk = rs.getTime("jammasuk");
                Time jamKeluar = rs.getTime("jam_keluar");

                // Konversi ke LocalTime
                LocalTime waktuMasuk = jamMasuk.toLocalTime();
                LocalTime waktuKeluar = jamKeluar.toLocalTime();

                // Menghitung selisih waktu dalam jam
                Duration selisihWaktu = Duration.between(waktuMasuk, waktuKeluar);

                // Jika hasil selisih negatif, tambahkan 24 jam
                if (selisihWaktu.isNegative()) {
                    selisihWaktu = selisihWaktu.plus(Duration.ofHours(24));
                }

                // Mengambil jenis kendaraan
                String jenisKendaraan = rs.getString("jeniskendaraan");

                // Mengambil tarif berdasarkan jenis kendaraan
                int tarifPerJam = 0;
                if (jenisKendaraan.equals("MOTOR")) {
                    tarifPerJam = 1500;
                } else if (jenisKendaraan.equals("MOBIL")) {
                    tarifPerJam = 2000;
                }

                // Menghitung biaya total
                int totalBiaya = (int) (selisihWaktu.toHours() * tarifPerJam);

                // Menampilkan biaya total di JTextField
                txtBayar.setText(Integer.toString(totalBiaya));

                // Menyimpan biaya total ke dalam database
                String updateQuery = "UPDATE parkirmasuk SET Biaya = ? WHERE notiket = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateQuery);
                updatePst.setInt(1, totalBiaya);
                updatePst.setString(2, tiketNumber);
                updatePst.executeUpdate();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public FormKendaraanKeluar() {
        initComponents();
        tampilkan_data();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelDataKeluar = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTiketKeluar = new javax.swing.JTextField();
        txtNoPlatKeluar = new javax.swing.JTextField();
        txtJenisKeluar = new javax.swing.JTextField();
        txtTglMasuk = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtJamMasuk = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtJamKeluar = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();
        btCari = new javax.swing.JButton();
        btKembali = new javax.swing.JButton();
        btBayar = new javax.swing.JButton();
        btKosongTabel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel1.setText("KENDARAAN KELUAR");

        tabelDataKeluar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabelDataKeluar);

        jLabel2.setText("No Tiket");

        jLabel3.setText("No Plat");

        jLabel4.setText("Jenis Kendaraan");

        jLabel5.setText("Tgl Masuk");

        jLabel6.setText("Jam Masuk");

        jLabel7.setText("Jam Keluar");

        txtJamKeluar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtJamKeluarFocusGained(evt);
            }
        });

        jLabel8.setText("Bayar");

        btCari.setText("Cari");
        btCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCariActionPerformed(evt);
            }
        });

        btKembali.setText("Parkir Masuk");
        btKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btKembaliActionPerformed(evt);
            }
        });

        btBayar.setText("Bayar");
        btBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBayarActionPerformed(evt);
            }
        });

        btKosongTabel.setText("Kosongkan Tabel");
        btKosongTabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btKosongTabelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(186, 186, 186)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtTglMasuk))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtJenisKeluar))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNoPlatKeluar))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtTiketKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btKosongTabel)
                                            .addComponent(txtJamMasuk))))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(70, 70, 70)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtJamKeluar)
                                                    .addComponent(txtBayar, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
                                                .addGap(22, 22, 22))
                                            .addComponent(btCari, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(btKembali))))
                .addGap(58, 58, 58))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTiketKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJamKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoPlatKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJenisKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTglMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btCari, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJamMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBayar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btKembali)
                    .addComponent(btKosongTabel))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKembaliActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new FormKendaraanMasuk().setVisible(true);
    }//GEN-LAST:event_btKembaliActionPerformed

    private void txtJamKeluarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtJamKeluarFocusGained
        // TODO add your handling code here:
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = sdf.format(new Date());

        // Menetapkan jam ke JTextField
        txtJamKeluar.setText(formattedTime);

        // Memanggil revalidate() dan repaint() untuk memperbarui antarmuka pengguna
        txtJamKeluar.revalidate();
        txtJamKeluar.repaint();
    }//GEN-LAST:event_txtJamKeluarFocusGained

    private void btCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCariActionPerformed
        // TODO add your handling code here:
        searchData("notiket", txtTiketKeluar.getText());
        
    }//GEN-LAST:event_btCariActionPerformed

    private void btBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBayarActionPerformed
        // TODO add your handling code here:
        calculateParkingFee();
        tampilkan_data();
    }//GEN-LAST:event_btBayarActionPerformed

    private void btKosongTabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKosongTabelActionPerformed
        // TODO add your handling code here:
        try
        {
        String sql = "DELETE FROM parkirmasuk WHERE Biaya > 0";
        java.sql.Connection conn =(Connection) Config.configDB();
            java.sql.PreparedStatement pstm =  conn.prepareStatement(sql);
            pstm.execute();
            JOptionPane.showMessageDialog(null, "Kosongkan Tabel Berhasil");
        }
        catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        tampilkan_data();
    }//GEN-LAST:event_btKosongTabelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormKendaraanKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormKendaraanKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormKendaraanKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormKendaraanKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormKendaraanKeluar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBayar;
    private javax.swing.JButton btCari;
    private javax.swing.JButton btKembali;
    private javax.swing.JButton btKosongTabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelDataKeluar;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtJamKeluar;
    private javax.swing.JTextField txtJamMasuk;
    private javax.swing.JTextField txtJenisKeluar;
    private javax.swing.JTextField txtNoPlatKeluar;
    private javax.swing.JTextField txtTglMasuk;
    private javax.swing.JTextField txtTiketKeluar;
    // End of variables declaration//GEN-END:variables

    private void connect() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
