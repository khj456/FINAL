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
 * {@code finalExam} 도서 대출 관리 시스템의 GUI를 설정하고 사용자가 도서를 관리할 수 있는 기능을 제공하는 클래스
 * 
 * @author Kim Hui Jin (khj1382443111@gmail.com)
 * @version 1.3
 * @since 1.0
 * 
 * @created 2024-12-23
 * @lastModified 2024-12-26
 * 
 * @changelog
 * <ul>
 * 	<li>2024-12-23: 최초 생성</li>
 * 	<li>2024-12-24: GUI 추가</li>
 * 	<li>2024-12-26: 버튼 위치 조정, 배경색 추가, 상단 문구 추가</li>
 * 	<li>2024-12-26: 배경색 변경, 로고 추가, 문구 색상 변경, 버튼색 추가</li>
 * 	<li>2024-12-26: 패널 정렬, 도서 목록 조회 내 대출 가능 여부 출력 추가, 도서 추가 오류 수정</li>
 * </ul>
 */

public class finalExam {
    private static HashMap<String, String[]> bookMap = new HashMap<>();
    private static ArrayList<String> borrowRecords = new ArrayList<>();
    private static final Color backgroundColor = new Color(0x1245AB);

    /**
     * <li>도서 대출 관리 시스템의 메인 메서드로, GUI를 초기화하고 이벤트 리스너를 설정</li>
     * <li>도서 목록을 CSV 파일에서 로드하기 위해 {@link #loadBooks()} 메서드를 호출</li>
     *
     * @param args 커맨드라인 인수
     *     
     * @created 2024-12-23
     * @lastModified 2024-12-26
     * 
     * @changelog
     * <ul>
     *   <li>2024-12-23: 최초 생성</li>
     *   <li>2024-12-26: 버튼 위치 정렬</li>
     *   <li>2024-12-26: 배경색, 로고, 문구 색상, 버튼색 추가</li>
     * </ul>
     */
    public static void main(String[] args) {
        loadBooks(); // 도서 목록을 CSV 파일에서 로드

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
            
            // 버튼 생성 및 설정
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

            // 중앙 패널에 버튼 추가
            centerPanel.add(viewBooksButton);
            centerPanel.add(borrowBookButton);
            centerPanel.add(returnBookButton);
            centerPanel.add(addBookButton);
            centerPanel.add(exitButton);

            mainPanel.add(centerPanel, BorderLayout.CENTER);
            mainPanel.add(topPanel, BorderLayout.NORTH);
            
            frame.add(mainPanel);

            // 버튼 클릭 시 이벤트 리스너 설정
            viewBooksButton.addActionListener(e -> viewBooks());
            borrowBookButton.addActionListener(e -> borrowBook());
            returnBookButton.addActionListener(e -> returnBook());
            addBookButton.addActionListener(e -> addBook());
            exitButton.addActionListener(e -> System.exit(0)); // 종료 버튼 클릭 시 애플리케이션 종료

            frame.setVisible(true);
        });
    }

    /**
     * <li>CSV 파일에서 도서 데이터를 로드하여 {@link bookMap}에 저장</li>
     * <li>각 도서의 제목, 저자, ISBN 및 대출 가능 여부를 설정</li>
     * 
     * @created 2024-12-23
     * @lastModified 2024-12-26
     * 
     * @throws IOException 파일 읽기 중 오류가 발생할 수 있음
     * 
     * @changelog
     * <ul>
     *   <li>2024-12-23: 최초 생성</li>
     *   <li>2024-12-26: 도서 로드 오류 메시지 추가</li>
     * </ul>
     */
    private static void loadBooks() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/booklist.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // CSV 파일에서 데이터 분리
                if (data.length == 3) { // 데이터가 3개일 경우만 처리
                    bookMap.put(data[0].trim(), new String[]{data[1].trim(), data[2].trim(), "true"}); // 도서 정보 추가
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "도서 데이터를 로드하는 중 오류가 발생했습니다."); // 오류 발생 시 메시지 표시
        }
    }

    /**
     * <li>현재 등록된 도서를 화면에 표시하는 새 JFrame을 생성</li>
     * <li>도서 목록이 비어 있을 경우 적절한 메시지를 표시</li>
     * 
     * @created 2024-12-23
     * @lastModified 2024-12-26
     * 
     * @changelog
     * <ul>
     *   <li>2024-12-23: 최초 생성</li>
     *   <li>2024-12-26: 배경색, 로고, 문구 색상, 버튼색 추가</li>
     *   <li>2024-12-26: 대출 가능 여부 출력 추가</li>
     * </ul>
     */
    private static void viewBooks() {
        JFrame frame = new JFrame("도서 목록");
        frame.setSize(500, 250);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        try {
            // 도서 목록 출력
            if (bookMap.isEmpty()) { // 도서 목록이 비어 있는 경우
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
        } catch (Exception e) { // 오류 발생 시 메시지 표시
            JOptionPane.showMessageDialog(frame, "오류 발생: " + e.getMessage());
        }

        frame.add(new JScrollPane(textArea)); // 텍스트 영역을 스크롤 가능하게 만들어 프레임에 추가
        frame.setVisible(true);
    }

    /**
     * <li>사용자가 도서를 대출할 수 있도록 회원 정보를 입력받는 JFrame을 생성</li>
     * <li>대출 가능 여부를 확인하고, 도서를 대출할 경우 상태를 업데이트</li>
     * 
     * @created 2024-12-23
     * @lastModified 2024-12-26
     * 
     * @changelog
     * <ul>
     *   <li>2024-12-23: 최초 생성</li>
     *   <li>2024-12-26: 배경색, 로고, 문구 색상, 버튼색 추가</li>
     *   <li>2024-12-26: 패널 정렬</li>
     * </ul>
     */
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

        // 패널에 구성 요소 추가
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(searchButton);

        frame.add(panel);
        frame.setVisible(true);

        // 검색 버튼 클릭 시 이벤트 리스너 설정
        searchButton.addActionListener(e -> {
            String title = titleField.getText().trim(); // 입력된 도서 제목 가져오기
            if (bookMap.containsKey(title)) { // 도서 제목이 bookMap에 존재하는지 확인
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
                    
                    // 패널에 회원 정보 구성 요소 추가
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

                    // 대출 버튼 클릭 시 이벤트 리스너 설정
                    borrowButton.addActionListener(b -> {
                        String memberInfo = nameField.getText() + " (" + idField.getText() + ")"; // 회원 정보 문자열 생성
                        borrowRecords.add(memberInfo);// 대출 기록에 추가

                        bookInfo[2] = "false"; // 도서의 대출 가능 여부를 'false'로 업데이트
                        bookMap.put(title, bookInfo); // 업데이트된 도서 정보를 다시 저장

                        JOptionPane.showMessageDialog(memberFrame, "도서를 대출했습니다.");
                        memberFrame.dispose();
                        frame.dispose();
                    });
                } else {
                    JOptionPane.showMessageDialog(frame, "이미 대출 중인 도서입니다."); // 대출 불가능 메시지 표시
                }
            } else {
                JOptionPane.showMessageDialog(frame, "일치하는 도서가 없습니다."); // 도서 미발견 메시지 표시
            }
        });
    }

    /**
     * <li>사용자가 도서를 반납할 수 있도록 회원 정보를 입력받는 JFrame을 생성</li>
     * <li>반납할 도서 제목을 입력받아 대출 가능 여부를 업데이트</li>
     * 
     * @created 2024-12-23
     * @lastModified 2024-12-26
     * 
     * @changelog
     * <ul>
     *   <li>2024-12-23: 최초 생성</li>
     *   <li>2024-12-26: 배경색, 로고, 문구 색상, 버튼색 추가</li>
     *   <li>2024-12-26: 패널 정렬</li>
     * </ul>
     */
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

        // 패널에 회원 정보 구성 요소 추가
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

        // 반납 버튼 클릭 시 이벤트 리스너 설정
        returnButton.addActionListener(e -> {
            String memberInfo = nameField.getText() + " (" + idField.getText() + ")";
            if (borrowRecords.contains(memberInfo)) { // 대출 기록에 회원 정보가 있는지 확인
                borrowRecords.remove(memberInfo); // 대출 기록에서 회원 정보 제거
                
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

                // 확인 버튼 클릭 시 이벤트 리스너 설정
                confirmButton.addActionListener(f -> {
                    String title = titleField.getText().trim(); // 입력된 도서 제목 가져오기
                    if (bookMap.containsKey(title)) { // 도서 목록에 도서가 존재하는지 확인
                        String[] bookInfo = bookMap.get(title); // 업데이트된 도서 정보를 다시 저장
                        
                        bookInfo[2] = "true"; // 도서의 대출 가능 여부를 'true'로 업데이트
                        bookMap.put(title, bookInfo);
                        
                        JOptionPane.showMessageDialog(titleFrame, "도서를 반납했습니다.");
                        titleFrame.dispose();
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(titleFrame, "해당 도서가 존재하지 않습니다.");
                    }
                });
            } else {
                JOptionPane.showMessageDialog(frame, "대출 기록이 없습니다.");
            }
        });
    }

    /**
     * <li>새로운 도서를 추가할 수 있도록 사용자로부터 정보를 입력받는 JFrame을 생성</li>
     * <li>입력된 도서 제목, 저자 및 ISBN을 사용하여 {@link bookMap}에 도서를 추가</li>
     * 
     * @created 2024-12-23
     * @lastModified 2024-12-26
     * 
     * @changelog
     * <ul>
     *   <li>2024-12-23: 최초 생성</li>
     *   <li>2024-12-26: 배경색, 로고, 문구 색상, 버튼색 추가</li>
     *   <li>2024-12-26: 패널 정렬</li>
     *   <li>2024-12-26: 도서를 추가하면 도서 목록 조회가 안되는 오류 수정</li>
     * </ul>
     */
    private static void addBook() {
        JFrame frame = new JFrame("도서 추가");
        frame.setSize(330, 190);
        frame.getContentPane().setBackground(backgroundColor);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(backgroundColor);
        
        // 도서 제목 패널
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

        JButton addButton = new JButton("추가");
        addButton.setBackground(Color.WHITE);

        // 패널에 추가
        panel.add(titlePanel);
        panel.add(authorPanel);
        panel.add(isbnPanel);
        panel.add(addButton);

        frame.add(panel);
        frame.setVisible(true);

        // 추가 버튼 클릭 시 이벤트 리스너 설정
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "모든 필드를 입력하세요."); // 입력이 비어 있을 경우 메시지 표시
                return;
            }
            
            if (bookMap.containsKey(title)) { // 이미 등록된 도서인지 확인
                JOptionPane.showMessageDialog(frame, "이미 등록된 도서입니다.");
            } else {
            	// 도서 정보 추가
            	bookMap.put(title, new String[]{author, isbn, "true"}); // 도서 정보를 bookMap에 추가
                JOptionPane.showMessageDialog(frame, "도서를 추가했습니다.");
                frame.dispose();
            }
        });
    }
}