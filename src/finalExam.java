import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class finalExam {
    private static HashMap<String, String[]> bookMap = new HashMap<>();
    private static ArrayList<String> borrowRecords = new ArrayList<>();

    public static void main(String[] args) {
        loadBooks();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("도서 대여 관리 시스템");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(5, 1));

            JButton viewBooksButton = new JButton("도서 목록 조회");
            JButton borrowBookButton = new JButton("도서 대출");
            JButton returnBookButton = new JButton("도서 반납");
            JButton addBookButton = new JButton("도서 추가");
            JButton exitButton = new JButton("종료");

            mainPanel.add(viewBooksButton);
            mainPanel.add(borrowBookButton);
            mainPanel.add(returnBookButton);
            mainPanel.add(addBookButton);
            mainPanel.add(exitButton);

            frame.add(mainPanel);

            // 버튼 이벤트 설정
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
                    bookMap.put(data[0].trim(), new String[]{data[1].trim(), data[2].trim()});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "도서 데이터를 로드하는 중 오류가 발생했습니다.");
        }
    }

    private static void viewBooks() {
        JFrame frame = new JFrame("도서 목록");
        frame.setSize(400, 300);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        for (Map.Entry<String, String[]> entry : bookMap.entrySet()) {
            String title = entry.getKey();
            String[] info = entry.getValue();
            textArea.append("제목: " + title + ", 저자: " + info[0] + ", ISBN: " + info[1] + "\n");
        }

        frame.add(new JScrollPane(textArea));
        frame.setVisible(true);
    }

    private static void borrowBook() {
        JFrame frame = new JFrame("도서 대출");
        frame.setSize(300, 200);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel titleLabel = new JLabel("도서 제목:");
        JTextField titleField = new JTextField();
        JButton searchButton = new JButton("검색");

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(searchButton);

        frame.add(panel);
        frame.setVisible(true);

        searchButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (bookMap.containsKey(title)) {
                JFrame memberFrame = new JFrame("회원 정보 입력");
                memberFrame.setSize(300, 200);

                JPanel memberPanel = new JPanel(new GridLayout(3, 2));
                JLabel nameLabel = new JLabel("이름:");
                JTextField nameField = new JTextField();
                JLabel idLabel = new JLabel("학번:");
                JTextField idField = new JTextField();
                JButton borrowButton = new JButton("대출");

                memberPanel.add(nameLabel);
                memberPanel.add(nameField);
                memberPanel.add(idLabel);
                memberPanel.add(idField);
                memberPanel.add(borrowButton);

                memberFrame.add(memberPanel);
                memberFrame.setVisible(true);

                borrowButton.addActionListener(b -> {
                    String memberInfo = nameField.getText() + " (" + idField.getText() + ")";
                    borrowRecords.add(memberInfo);
                    JOptionPane.showMessageDialog(memberFrame, "도서를 대출했습니다.");
                    memberFrame.dispose();
                });
            } else {
                JOptionPane.showMessageDialog(frame, "일치하는 도서가 없습니다.");
            }
        });
    }

    private static void returnBook() {
        JFrame frame = new JFrame("도서 반납");
        frame.setSize(300, 200);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel nameLabel = new JLabel("이름:");
        JTextField nameField = new JTextField();
        JLabel idLabel = new JLabel("학번:");
        JTextField idField = new JTextField();
        JButton returnButton = new JButton("반납");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(idLabel);
        panel.add(returnButton);

        frame.add(panel);
        frame.setVisible(true);

        returnButton.addActionListener(e -> {
            String memberInfo = nameField.getText() + " (" + idField.getText() + ")";
            if (borrowRecords.contains(memberInfo)) {
                borrowRecords.remove(memberInfo);
                JOptionPane.showMessageDialog(frame, "도서를 반납했습니다.");
            } else {
                JOptionPane.showMessageDialog(frame, "대출 기록이 없습니다.");
            }
        });
    }

    private static void addBook() {
        JFrame frame = new JFrame("도서 추가");
        frame.setSize(300, 200);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        JLabel titleLabel = new JLabel("도서 제목:");
        JTextField titleField = new JTextField();
        JLabel authorLabel = new JLabel("저자:");
        JTextField authorField = new JTextField();
        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbnField = new JTextField();
        JButton addButton = new JButton("추가");

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(authorLabel);
        panel.add(authorField);
        panel.add(isbnLabel);
        panel.add(isbnField);
        panel.add(addButton);

        frame.add(panel);
        frame.setVisible(true);

        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();

            if (bookMap.containsKey(title)) {
                JOptionPane.showMessageDialog(frame, "이미 등록된 도서입니다.");
            } else {
                bookMap.put(title, new String[]{author, isbn});
                JOptionPane.showMessageDialog(frame, "도서를 추가했습니다.");
            }
        });
    }
}