import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;
import java.awt.Color;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class ClientGUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField portField;
	private JTextField isbnField;
	private JTextField titleField;
	private JTextField authorField;
	private JTextField publisherField;
	private JTextField yearField;
	private JFormattedTextField ipField;
	private JButton btnDisconnect;
	private JButton btnConnect;
	private JButton btnSend;
	private ButtonGroup opButtonGroup;
	private JRadioButton radioSubmit;
	private JRadioButton radioRemove;
	private JRadioButton radioGet;
	private JRadioButton radioGetAll;
	private JRadioButton radioUpdate;
	private JTextArea serverTextArea;

	/**
	 * Create the application.
	 */
	public ClientGUI(String title) {
		super.setTitle(title);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		BorderLayout borderLayout = (BorderLayout) getContentPane().getLayout();
		borderLayout.setHgap(10);
		setResizable(false);
		setBounds(100, 100, 650, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel connectPanel = new JPanel();
		connectPanel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		getContentPane().add(connectPanel, BorderLayout.NORTH);
		
		JPanel ipPanel = new JPanel();
		connectPanel.add(ipPanel);
		
		JLabel labelIpAddress = new JLabel("IP");
		ipPanel.add(labelIpAddress);
		
		ipField = new JFormattedTextField();
		ipField.setColumns(12);
		ipField.setText("0.0.0.0");
		ipPanel.add(ipField);
		
		JPanel portPanel = new JPanel();
		connectPanel.add(portPanel);
		
		JLabel labelPort = new JLabel("Port");
		portPanel.add(labelPort);
		
		portField = new JTextField();
		portPanel.add(portField);
		portField.setColumns(10);
		
		btnConnect = new JButton("Connect");
		connectPanel.add(btnConnect);
		
		JPanel serverPanel = new JPanel();
		serverPanel.setLayout(new BoxLayout(serverPanel, BoxLayout.X_AXIS));
		serverPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		getContentPane().add(serverPanel, BorderLayout.SOUTH);
		
		JLabel lblServerResponse = new JLabel("Server Response");
		serverPanel.add(lblServerResponse);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(250, 100));
		serverPanel.add(scrollPane);
		
		serverTextArea = new JTextArea();
		serverTextArea.setFont(serverTextArea.getFont().deriveFont(12f));
		serverTextArea.setEditable(false);
		scrollPane.setViewportView(serverTextArea);
		
		btnDisconnect = new JButton("Disconnect");
		serverPanel.add(btnDisconnect);
		
		JPanel operationPanel = new JPanel();
		operationPanel.setBorder(new MatteBorder(0, 0, 0, 1, (Color) new Color(0, 0, 0)));
		getContentPane().add(operationPanel, BorderLayout.WEST);
		operationPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel labelOperation = new JLabel("Operation");
		labelOperation.setHorizontalAlignment(SwingConstants.CENTER);
		operationPanel.add(labelOperation, BorderLayout.NORTH);
		
		JPanel opButtons = new JPanel();
		opButtons.setPreferredSize(new Dimension(100, 10));
		operationPanel.add(opButtons, BorderLayout.CENTER);
		opButtons.setLayout(new GridLayout(0, 1, 0, 0));
		
		radioSubmit = new JRadioButton("SUBMIT");
		opButtons.add(radioSubmit);
		
		radioUpdate = new JRadioButton("UPDATE");
		opButtons.add(radioUpdate);
		
		radioRemove = new JRadioButton("REMOVE");
		opButtons.add(radioRemove);
		
		radioGet = new JRadioButton("GET");
		opButtons.add(radioGet);
		
		radioGetAll = new JRadioButton("GET ALL");
		opButtons.add(radioGetAll);
		
		opButtonGroup = new ButtonGroup();
		radioSubmit.setActionCommand("SUBMIT");
		radioUpdate.setActionCommand("UPDATE");
		radioRemove.setActionCommand("REMOVE");
		radioGet.setActionCommand("GET");
		radioGetAll.setActionCommand("GET\nALL");
		opButtonGroup.add(radioSubmit);
		opButtonGroup.add(radioUpdate);
		opButtonGroup.add(radioRemove);
		opButtonGroup.add(radioGet);
		opButtonGroup.add(radioGetAll);
		
		JPanel sendPanel = new JPanel();
		sendPanel.setBorder(new MatteBorder(0, 1, 0, 0, (Color) new Color(0, 0, 0)));
		getContentPane().add(sendPanel, BorderLayout.EAST);
		
		btnSend = new JButton("Send");
		GroupLayout gl_sendPanel = new GroupLayout(sendPanel);
		gl_sendPanel.setHorizontalGroup(
			gl_sendPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_sendPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btnSend)
					.addContainerGap())
		);
		gl_sendPanel.setVerticalGroup(
			gl_sendPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sendPanel.createSequentialGroup()
					.addGap(84)
					.addComponent(btnSend)
					.addContainerGap(90, Short.MAX_VALUE))
		);
		sendPanel.setLayout(gl_sendPanel);
		
		JPanel attributePanel = new JPanel();
		attributePanel.setBorder(new EmptyBorder(1, 1, 1, 0));
		attributePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		getContentPane().add(attributePanel, BorderLayout.CENTER);
		
		JLabel labelIsbn = new JLabel("ISBN");
		
		isbnField = new JTextField();
		isbnField.setColumns(10);
		
		JLabel labelTitle = new JLabel("Title");
		
		titleField = new JTextField();
		titleField.setColumns(10);
		
		JLabel labelAuthor = new JLabel("Author");
		
		authorField = new JTextField();
		authorField.setColumns(10);
		
		JLabel labelPublisher = new JLabel("Publisher");
		
		publisherField = new JTextField();
		publisherField.setColumns(10);
		
		JLabel labelYear = new JLabel("Year");
		
		yearField = new JTextField();
		yearField.setColumns(10);
		
		JLabel labelAttributes = new JLabel("Attributes");
		GroupLayout gl_attributePanel = new GroupLayout(attributePanel);
		gl_attributePanel.setHorizontalGroup(
			gl_attributePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_attributePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_attributePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_attributePanel.createSequentialGroup()
							.addComponent(labelYear)
							.addGap(37)
							.addComponent(yearField, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
						.addGroup(gl_attributePanel.createSequentialGroup()
							.addComponent(labelPublisher)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(publisherField, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
						.addGroup(gl_attributePanel.createSequentialGroup()
							.addComponent(labelAuthor)
							.addGap(21)
							.addComponent(authorField, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
						.addGroup(gl_attributePanel.createSequentialGroup()
							.addComponent(labelTitle)
							.addGap(36)
							.addComponent(titleField, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
						.addGroup(gl_attributePanel.createSequentialGroup()
							.addComponent(labelIsbn)
							.addGap(36)
							.addComponent(isbnField, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
						.addComponent(labelAttributes))
					.addContainerGap())
		);
		gl_attributePanel.setVerticalGroup(
			gl_attributePanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_attributePanel.createSequentialGroup()
					.addComponent(labelAttributes)
					.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
					.addGroup(gl_attributePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelIsbn)
						.addComponent(isbnField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_attributePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelTitle)
						.addComponent(titleField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_attributePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelAuthor)
						.addComponent(authorField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_attributePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelPublisher)
						.addComponent(publisherField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_attributePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelYear)
						.addComponent(yearField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18))
		);
		attributePanel.setLayout(gl_attributePanel);
	}

	public JTextField getPortField() {
		return portField;
	}

	public JTextField getIsbnField() {
		return isbnField;
	}

	public JTextField getTitleField() {
		return titleField;
	}

	public JTextField getAuthorField() {
		return authorField;
	}

	public JTextField getPublisherField() {
		return publisherField;
	}

	public JTextField getYearField() {
		return yearField;
	}

	public JFormattedTextField getIpField() {
		return ipField;
	}

	public JButton getBtnDisconnect() {
		return btnDisconnect;
	}

	public JButton getBtnConnect() {
		return btnConnect;
	}

	public JButton getBtnSend() {
		return btnSend;
	}

	public ButtonGroup getOpButtons() {
		return opButtonGroup;
	}

	public JTextArea getServerTextArea() {
		return serverTextArea;
	}
}
