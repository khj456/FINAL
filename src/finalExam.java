import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * 도서 관리 프로그램입니다.
 * 
 * @author Kim Hui Jin (khj1382443111@gmail.com)
 * @version 1.2
 * @since 1.0
 * 
 * @created 2024-12-23
 * @lastModified 2024-12-24
 * 
 * @changelog
 * <ul>
 * 		<li>2024-12-23: 최초 생성</li>
 * 		<li>2024-12-24: GUI 추가</li>
 * </ul>
 */

public class finalExam {
    private static HashMap<String, String[]> bookMap = new HashMap<>();
    private static ArrayList<String> borrowRecords = new ArrayList<>();
    private static final Color backgroundColor = new Color(0x1245AB);

    public static void main(String[] args) {
        loadBooks();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("도서 대출 관리 시스템");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBackground(backgroundColor);
            
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new GridLayout(5, 1, 10, 10));
            centerPanel.setBorder(new EmptyBorder(20, 50, 50, 50));
            centerPanel.setBackground(backgroundColor);
            
            ImageIcon image = new ImageIcon("src/로고.png");
            JPanel topPanel = new JPanel();
            topPanel.setBackground(backgroundColor);
            JLabel topLabel = new JLabel("도서 관리", image, JLabel.CENTER);
            
            Font font = new Font(Font.DIALOG, Font.BOLD, 30);
            topLabel.setFont(font);
            topLabel.setForeground(Color.WHITE);
            topPanel.add(topLabel);
            		
            JButton viewBooksButton = new JButton("도서 목록 조회");
            viewBooksButton.setBackground(Color.WHITE);
            JButton borrowBookButton = new JButton("도서 대출");
            borrowBookButton.setBackground(Color.WHITE);
            JButton returnBookButton = new JButton("도서 반납");
            returnBookButton.setBackground(Color.WHITE);
            JButton addBookButton = new JButton("도서 추가");
            addBookButton.setBackground(Color.WHITE);
            JButton exitButton = new JButton("종료");
            exitButton.setBackground(Color.WHITE);

            centerPanel.add(viewBooksButton);
            centerPanel.add(borrowBookButton);
            centerPanel.add(returnBookButton);
            centerPanel.add(addBookButton);
            centerPanel.add(exitButton);

            mainPanel.add(centerPanel, BorderLayout.CENTER);
            mainPanel.add(topPanel, BorderLayout.NORTH);
            
            frame.add(mainPanel);

            viewBooksButton.addActionListener(e -> viewBooks());
            borrowBookButton.addActionListener(e -> borrowBook());
            returnBookButton.addActionListener(e -> returnBook());
            addBookButton.addActionListener(e -> addBook());
            exitButton.addActionListener(e -> System.exit(0));

            frame.setVisible(true);
        });
    }

    private static void loadBooks() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/booklist.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    bookMap.put(data[0].trim(), new String[]{data[1].trim(), data[2].trim(), "true"});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "도서 데이터를 로드하는 중 오류가 발생했습니다.");
        }
    }

    private static void viewBooks() {
        JFrame frame = new JFrame("도서 목록");
        frame.setSize(500, 250);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        try {
            // 도서 목록 출력
            if (bookMap.isEmpty()) {
                textArea.setText("등록된 도서가 없습니다.");
            } else {
                for (Map.Entry<String, String[]> entry : bookMap.entrySet()) {
                    String title = entry.getKey();
                    String[] info = entry.getValue();
                    String author = info[0];
                    String isbn = info[1];
                    String availability = info[2].equals("true") ? "가능" : "불가능";

                    textArea.append(String.format("제목: %s, 저자: %s, ISBN: %s, 대출 가능 여부: %s\n", title, author, isbn, availability));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "오류 발생: " + e.getMessage());
        }

        frame.add(new JScrollPane(textArea));
        frame.setVisible(true);
    }

    private static void borrowBook() {
        JFrame frame = new JFrame("도서 대출");
        frame.setSize(370, 100);
        frame.getContentPane().setBackground(backgroundColor);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(backgroundColor);
        
        JLabel titleLabel = new JLabel("도서 제목:");
        titleLabel.setForeground(Color.WHITE);
        JTextField titleField = new JTextField(15);
        JButton searchButton = new JButton("검색");
        searchButton.setBackground(Color.WHITE);

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(searchButton);

        frame.add(panel);
        frame.setVisible(true);

        searchButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (bookMap.containsKey(title)) {
                String[] bookInfo = bookMap.get(title);

                if (bookInfo[2].equals("true")) { // 대출 가능 여부 확인
                    JFrame memberFrame = new JFrame("회원 정보 입력");
                    memberFrame.setSize(280, 180);

                    JPanel memberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
                    memberPanel.setBackground(backgroundColor);
                    
                    JLabel nameLabel = new JLabel("이름:");
                    nameLabel.setForeground(Color.WHITE);
                    JTextField nameField = new JTextField(15);
                    JLabel idLabel = new JLabel("학번:");
                    idLabel.setForeground(Color.WHITE);
                    JTextField idField = new JTextField(15);
                    JButton borrowButton = new JButton("대출");
                    borrowButton.setBackground(Color.WHITE);
                    
                    JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    namePanel.setBackground(backgroundColor);
                    namePanel.add(nameLabel);
                    namePanel.add(nameField);
                    
                    JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    idPanel.setBackground(backgroundColor);
                    idPanel.add(idLabel);
                    idPanel.add(idField);

                    memberPanel.add(namePanel);
                    memberPanel.add(idPanel);
                    memberPanel.add(borrowButton);

                    memberFrame.add(memberPanel);
                    memberFrame.setVisible(true);

                    borrowButton.addActionListener(b -> {
                        String memberInfo = nameField.getText() + " (" + idField.getText() + ")";
                        borrowRecords.add(memberInfo);

                        // 대출 가능 여부를 "false"로 업데이트
                        bookInfo[2] = "false";
                        bookMap.put(title, bookInfo);

                        JOptionPane.showMessageDialog(memberFrame, "도서를 대출했습니다.");
                        memberFrame.dispose();
                        frame.dispose();
                    });
                } else {
                    JOptionPane.showMessageDialog(frame, "이미 대출 중인 도서입니다.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "일치하는 도서가 없습니다.");
            }
        });
    }

    private static void returnBook() {
        JFrame frame = new JFrame("도서 반납");
        frame.setSize(280, 180);
        frame.getContentPane().setBackground(backgroundColor);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(backgroundColor);
        
        JLabel nameLabel = new JLabel("이름:");
        nameLabel.setForeground(Color.WHITE);
        JTextField nameField = new JTextField(15);
        JLabel idLabel = new JLabel("학번:");
        idLabel.setForeground(Color.WHITE);
        JTextField idField = new JTextField(15);
        JButton returnButton = new JButton("반납");
        returnButton.setBackground(Color.WHITE);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBackground(backgroundColor);
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.setBackground(backgroundColor);
        idPanel.add(idLabel);
        idPanel.add(idField);

        panel.add(namePanel);
        panel.add(idPanel);
        panel.add(returnButton);

        frame.add(panel);
        frame.setVisible(true);

        returnButton.addActionListener(e -> {
            String memberInfo = nameField.getText() + " (" + idField.getText() + ")";
            if (borrowRecords.contains(memberInfo)) {
                borrowRecords.remove(memberInfo); // 대출 기록 제거
                
                // 새로운 프레임을 생성하여 도서 제목 입력 받기
                JFrame titleFrame = new JFrame("반납할 도서 제목 입력");
                titleFrame.setSize(370, 100);
                titleFrame.getContentPane().setBackground(backgroundColor);

                JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                titlePanel.setBackground(backgroundColor);
                JLabel titleLabel = new JLabel("도서 제목:");
                titleLabel.setForeground(Color.WHITE);
                JTextField titleField = new JTextField(15);
                JButton confirmButton = new JButton("확인");
                confirmButton.setBackground(Color.WHITE);

                titlePanel.add(titleLabel);
                titlePanel.add(titleField);
                titlePanel.add(confirmButton);

                titleFrame.add(titlePanel);
                titleFrame.setVisible(true);

                confirmButton.addActionListener(f -> {
                    String title = titleField.getText().trim();
                    if (bookMap.containsKey(title)) {
                        String[] bookInfo = bookMap.get(title);
                        
                        // 대출 가능 여부를 "true"로 업데이트
                        bookInfo[2] = "true";
                        bookMap.put(title, bookInfo);
                        
                        JOptionPane.showMessageDialog(titleFrame, "도서를 반납했습니다.");
                        titleFrame.dispose(); // 입력 창 닫기
                        frame.dispose(); // 회원 정보 창 닫기
                    } else {
                        JOptionPane.showMessageDialog(titleFrame, "해당 도서가 존재하지 않습니다.");
                    }
                });
            } else {
                JOptionPane.showMessageDialog(frame, "대출 기록이 없습니다.");
            }
        });
    }

    private static void addBook() {
        JFrame frame = new JFrame("도서 추가");
        frame.setSize(330, 190);
        frame.getContentPane().setBackground(backgroundColor);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(backgroundColor);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(backgroundColor);
        JLabel titleLabel = new JLabel("도서 제목:");
        titleLabel.setForeground(Color.WHITE);
        JTextField titleField = new JTextField(15);
        titlePanel.add(titleLabel);
        titlePanel.add(titleField);

        // 저자 패널
        JPanel authorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        authorPanel.setBackground(backgroundColor);
        JLabel authorLabel = new JLabel("저자:");
        authorLabel.setForeground(Color.WHITE);
        JTextField authorField = new JTextField(15);
        authorPanel.add(authorLabel);
        authorPanel.add(authorField);

        // ISBN 패널
        JPanel isbnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        isbnPanel.setBackground(backgroundColor);
        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setForeground(Color.WHITE);
        JTextField isbnField = new JTextField(15);
        isbnPanel.add(isbnLabel);
        isbnPanel.add(isbnField);

        // 추가 버튼
        JButton addButton = new JButton("추가");
        addButton.setBackground(Color.WHITE);

        // 패널에 추가
        panel.add(titlePanel);
        panel.add(authorPanel);
        panel.add(isbnPanel);
        panel.add(addButton);

        frame.add(panel);
        frame.setVisible(true);

        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "모든 필드를 입력하세요.");
                return; // 입력이 잘못된 경우 추가하지 않음
            }
            
            if (bookMap.containsKey(title)) {
                JOptionPane.showMessageDialog(frame, "이미 등록된 도서입니다.");
            } else {
            	bookMap.put(title, new String[]{author, isbn, "true"});
                JOptionPane.showMessageDialog(frame, "도서를 추가했습니다.");
                frame.dispose();
                System.out.println("현재 도서 목록: " + bookMap);
            }
        });
    }
}