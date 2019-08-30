package com.googlecode.fileconvert.view;

import com.googlecode.fileconvert.controller.Context;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author 陈佳佳
 * @email chenjiajia_1@126.com
 * @date 2011-07-31
 */
public class MainFrame extends javax.swing.JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 729624065839956916L;

	/**
	 * Creates new form MainFrame
	 */
	public MainFrame() {
		initComponents();
	}


	private void initComponents() {
		String nativeLF = UIManager.getSystemLookAndFeelClassName();
	    try {
			UIManager.setLookAndFeel(nativeLF);
		} catch (Exception e) {

		}

		jFrame=this;
		context=new Context(this);
		jPanel1 = new javax.swing.JPanel();
		jPanel8 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jPanel9 = new javax.swing.JPanel();
		jComboBox1 = new javax.swing.JComboBox();
		jComboBox2 = new javax.swing.JComboBox();
		jPanel2 = new javax.swing.JPanel();
		jPanel6 = new javax.swing.JPanel();
		jLabel4 = new javax.swing.JLabel();
		jComboBox3 = new javax.swing.JComboBox();
		jPanel3 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		jLabel3 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jComboBox1.setEditable(true);
		jComboBox2.setEditable(true);
		jComboBox3.setEditable(true);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("\u6587\u4ef6\u7f16\u7801\u6279\u91cf\u8f6c\u6362"+"-------:chenjiajia_1@126.com");
		setFocusTraversalPolicyProvider(true);
		setFont(new java.awt.Font("微软雅黑", 1, 18));
		setForeground(new java.awt.Color(255, 255, 255));
		setLocationByPlatform(true);
		setResizable(false);

		jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jPanel1.setLayout(new java.awt.GridLayout(2, 1));

		jPanel8.setLayout(new java.awt.GridLayout(1, 0));

		jLabel1.setFont(new java.awt.Font("宋体", 1, 18));
		jLabel1.setText("1.\u9009\u62e9\u6e90\u6587\u4ef6\u7f16\u7801");
		jPanel8.add(jLabel1);

		jLabel2.setFont(new java.awt.Font("宋体", 1, 18));
		jLabel2.setText("2.\u9009\u62e9\u76ee\u6807\u6587\u4ef6\u7f16\u7801");
		jPanel8.add(jLabel2);

		jPanel1.add(jPanel8);

		jPanel9.setLayout(new java.awt.GridLayout(1, 0));

		jComboBox1.setFont(new java.awt.Font("宋体", 1, 18));
		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"自动侦测", "GBK", "UTF-8", "UTF-8 BOM", "ISO-8859-1", "Unicode", "BIG5" }));
		jComboBox1.setPreferredSize(new java.awt.Dimension(94, 35));
		jPanel9.add(jComboBox1);

		jComboBox2.setFont(new java.awt.Font("宋体", 1, 18));
		jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"UTF-8", "UTF-8 BOM", "GBK", "ISO-8859-1", "Unicode", "BIG5" }));
		jComboBox2.setPreferredSize(new java.awt.Dimension(94, 35));
		jPanel9.add(jComboBox2);

		jPanel1.add(jPanel9);

		jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jPanel2.setLayout(new java.awt.GridLayout(1, 0));

		jPanel6.setLayout(new java.awt.GridLayout(1, 0));

		jLabel4.setFont(new java.awt.Font("宋体", 1, 18));
		jLabel4.setText("3.正则表达式过滤");
		jPanel6.add(jLabel4);

		jComboBox3.setFont(new java.awt.Font("宋体", 1, 18));
		jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"", "^.+java$", "^.+txt$","^.+(java|txt)$","^.+(java|txt|properties)$" }));
		jComboBox3.setPreferredSize(new java.awt.Dimension(200, 35));
		jPanel6.add(jComboBox3);

		jPanel2.add(jPanel6);

		jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jPanel3.setLayout(new java.awt.GridLayout(2, 1));

		jPanel4.setLayout(new java.awt.GridLayout(1, 0));

		jButton1.setFont(new java.awt.Font("宋体", 1, 18));
		jButton1.setText("4.选择目录或文件");
		jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jButton1MouseClicked(evt);
			}
		});
		jPanel4.add(jButton1);

		jButton2.setFont(new java.awt.Font("宋体", 1, 18));
		jButton2.setForeground(new java.awt.Color(0, 51, 153));
		jButton2.setText("5.\u8f6c\u6362");
		jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jButton2MouseClicked(evt);
			}
		});
		jPanel4.add(jButton2);

		jPanel3.add(jPanel4);

		jLabel3.setFont(new java.awt.Font("宋体", 1, 18));
		jLabel3.setText("\u6587\u4ef6\u8def\u5f84:");
		jPanel5.add(jLabel3);

		jTextField1.setEditable(true);
		jTextField1.setFont(new java.awt.Font("宋体", 1, 12));
		jTextField1.setPreferredSize(new java.awt.Dimension(300, 35));
		jPanel5.add(jTextField1);

		jPanel3.add(jPanel5);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jPanel2,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														405, Short.MAX_VALUE)
												.addComponent(
														jPanel1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														405, Short.MAX_VALUE)
												.addComponent(
														jPanel3,
														javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														405, Short.MAX_VALUE))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jPanel1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										88,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(5, 5, 5)
								.addComponent(jPanel2,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										50,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jPanel3,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));

		pack();
	}

    /**
     * 转换按钮事件处理代码
     * @param evt
     */
	private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {
		if (jTextField1.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(jFrame, "请选择目录");
			return;
		}
		context.setSoureCode(jComboBox1.getSelectedItem().toString());
		context.setTargetCode(jComboBox2.getSelectedItem().toString());
		context.setFilter(jComboBox3.getSelectedItem().toString());
		context.setPath(jTextField1.getText().trim());
		boolean ok = context.convert();
		if (ok) {
			JOptionPane.showMessageDialog(jFrame, "转换成功");
		}

	}

    /**
     *   选择目录或文件按钮处理代码
     * @param evt
     */
	private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {

		JFileChooser jfChooser = new JFileChooser();
		jfChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfChooser.setDialogTitle("选择目录或文件");
		jfChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return true;
			}

			@Override
			public String getDescription() {

				return "文件夹或文本文件";
			}
		});
		int result = jfChooser.showOpenDialog(jFrame);
		if (result == JFileChooser.APPROVE_OPTION) { // 确认打开

			File fileIn = jfChooser.getSelectedFile();

			if (fileIn.exists()) {
				jTextField1.setText(fileIn.getPath());
				
			} else {
				JOptionPane.showMessageDialog(jFrame, "目录不存在");
			}
		} else if (result == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(jFrame, "选择出错,请重选!");
		}

	}


	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainFrame().setVisible(true);

			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JComboBox jComboBox1;
	private javax.swing.JComboBox jComboBox2;
	private javax.swing.JComboBox jComboBox3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JTextField jTextField1;
	private JFrame jFrame;
	// End of variables declaration//GEN-END:variables
	Context context;
}