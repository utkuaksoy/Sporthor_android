//
//  CommentViewController.swift
//  Sporthor
//
//  Created by derTurke on 29.04.2025.
//
//

import UIKit
import ComponentKit
import IQKeyboardManagerSwift
import PanModal

final class CommentViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CommentPresenterProtocol {
        get { return self.basePresenter as! CommentPresenterProtocol }
        set { self.basePresenter = newValue }
    }

    // MARK: - UI Elements
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundSub300.color
        view.setCornerRadius(2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: .black, font: .heading06)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()

    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.dataSource = self
        tableView.delegate = self
        tableView.showsVerticalScrollIndicator = false
        tableView.allowsSelection = false
        tableView.separatorStyle = .none
        tableView.removeEmptyCell()
        tableView.translatesAutoresizingMaskIntoConstraints = false
        return tableView
    }()

    private lazy var inputProfileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.setCornerRadius(24)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()

    private lazy var inputTextView: CKCustomTextView = {
        let textView = CKCustomTextView(
            customDelegate: self,
            textColor: DesignKitColorName.contentStrong900.color,
            placeholder: "Yorum Yaz",
            placeholderColor: DesignKitColorName.contentSoft600.color,
            font: .body04Compact,
            backgroundColor: DesignKitColorName.backgroundWeak100.color,
            borderWidth: 1,
            borderColor: DesignKitColorName.borderSoft200.color,
            cornerRadius: 24,
            padding: 16,
            minHeight: 48,
            maxHeight: 144
        )
        textView.translatesAutoresizingMaskIntoConstraints = false
        return textView
    }()

    private lazy var inputSendButton: CKButton = {
        let button = CKButton(
            delegate: self,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 24,
            image: Asset.arrowTop.image)
        button.isHidden = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var inputAreaTopSeperator: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private var inputTextViewBottomConstraint: NSLayoutConstraint!
    private var inputSendButtonWidthConstraint: NSLayoutConstraint!
    private var inputSendButtonTrailingConstraint: NSLayoutConstraint!

    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        IQKeyboardManager.shared.enable = false
        IQKeyboardManager.shared.enableAutoToolbar = false

        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(_:)),
                                               name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide(_:)),
                                               name: UIResponder.keyboardWillHideNotification, object: nil)
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.enableAutoToolbar = true
        NotificationCenter.default.removeObserver(self)
    }
    
    @objc private func keyboardWillShow(_ notification: Notification) {
        guard let userInfo = notification.userInfo,
              let keyboardFrame = userInfo[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect,
              let duration = userInfo[UIResponder.keyboardAnimationDurationUserInfoKey] as? TimeInterval,
              let curve = userInfo[UIResponder.keyboardAnimationCurveUserInfoKey] as? UInt else { return }

        let keyboardHeight = keyboardFrame.height
        inputTextViewBottomConstraint.constant = -keyboardHeight + 24

        UIView.animate(withDuration: duration,
                       delay: 0,
                       options: UIView.AnimationOptions(rawValue: curve << 16),
                       animations: {
            self.view.layoutIfNeeded()
        })
    }

    @objc private func keyboardWillHide(_ notification: Notification) {
        guard let userInfo = notification.userInfo,
              let duration = userInfo[UIResponder.keyboardAnimationDurationUserInfoKey] as? TimeInterval,
              let curve = userInfo[UIResponder.keyboardAnimationCurveUserInfoKey] as? UInt else { return }

        inputTextViewBottomConstraint.constant = 0

        UIView.animate(withDuration: duration,
                       delay: 0,
                       options: UIView.AnimationOptions(rawValue: curve << 16),
                       animations: {
            self.view.layoutIfNeeded()
        })
    }
}

// MARK: - CommentPresenterDelegate
extension CommentViewController: CommentPresenterDelegate {
    func didSetProfilePhoto(_ image: String) {
        inputProfileImageView.setImage(with: image, placeholder: Asset.errorUserImage.image)
    }
    func didSetTitleLabelText(_ title: String) {
        titleLabel.text = title
    }

    func prepareUI() {
        view.addSubview(scrollLineView)
        view.addSubview(titleLabel)
        view.addSubview(tableView)
        view.addSubview(inputProfileImageView)
        view.addSubview(inputTextView)
        view.addSubview(inputSendButton)
        view.addSubview(inputAreaTopSeperator)
        
        inputTextViewBottomConstraint = inputTextView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        inputSendButtonWidthConstraint = inputSendButton.widthAnchor.constraint(equalToConstant: 0)
        inputSendButtonTrailingConstraint = inputSendButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -8)

        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            titleLabel.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 16),
            titleLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),

            tableView.topAnchor.constraint(equalTo: titleLabel.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: inputAreaTopSeperator.topAnchor),

            inputAreaTopSeperator.bottomAnchor.constraint(equalTo: inputTextView.topAnchor, constant: -8),
            inputAreaTopSeperator.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            inputAreaTopSeperator.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            inputAreaTopSeperator.heightAnchor.constraint(equalToConstant: 1),

            inputProfileImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            inputProfileImageView.bottomAnchor.constraint(equalTo: inputTextView.bottomAnchor),
            inputProfileImageView.widthAnchor.constraint(equalToConstant: 48),
            inputProfileImageView.heightAnchor.constraint(equalToConstant: 48),

            inputSendButtonTrailingConstraint,
            inputSendButton.bottomAnchor.constraint(equalTo: inputTextView.bottomAnchor),
            inputSendButtonWidthConstraint,
            inputSendButton.heightAnchor.constraint(equalToConstant: 48),

            inputTextView.leadingAnchor.constraint(equalTo: inputProfileImageView.trailingAnchor, constant: 8),
            inputTextView.trailingAnchor.constraint(equalTo: inputSendButton.leadingAnchor, constant: -8),
            inputTextViewBottomConstraint,
            inputTextView.heightAnchor.constraint(greaterThanOrEqualToConstant: 48)
        ])
    }

    func changeInputSendButtonHidden(_ isHidden: Bool) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            
            self.inputSendButton.isHidden = isHidden
            
            self.inputSendButtonWidthConstraint.constant = isHidden ? 0 : 48
            self.inputSendButtonTrailingConstraint.constant = isHidden ? -8 : -16
            
            UIView.animate(withDuration: 0.2) {
                self.view.layoutIfNeeded()
            }
        }
    }

    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
    
    func clearTextField() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            inputTextView.text = ""
            self.textViewDidChange(self.inputTextView)
        }
    }
}

// MARK: - UITableViewDataSource && UITableViewDelegate
extension CommentViewController: UITableViewDataSource, UITableViewDelegate{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.comments.isEmpty ? 1 : presenter.comments.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if presenter.comments.isEmpty {
            let cell = EmptyTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(image: Asset.comment.image, description: "Hiç yorum yok")
            return cell
        } else {
            let cell = CommentTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(model: presenter.comments[indexPath.row])
            return cell
        }
    }
}

// MARK: - CKTextFieldDelegate
extension CommentViewController: CKCustomTextViewDelegate {
    func textViewDidChange(_ ckCustomTextView: CKCustomTextView) {
        guard let text = ckCustomTextView.text else { return }
        UIView.performWithoutAnimation {
            ckCustomTextView.invalidateIntrinsicContentSize()
            view.layoutIfNeeded()
        }
        presenter.textFieldDidChangeSelection(text)
    }
    
    func textViewDidEndEditing(_ ckCustomTextView: CKCustomTextView) {
        guard let text = ckCustomTextView.text else { return }
        presenter.textFieldDidEndEditing(text)
    }
}

// MARK: - CKButtonDelegate
extension CommentViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        inputTextView.resignFirstResponder()
        presenter.didTappedSendButton()
    }
}

// MARK: - PanModalPresentable
extension CommentViewController: PanModalPresentable {
    var allowsExtendedPanScrolling: Bool {
        true
    }
    
    var panScrollable: UIScrollView? {
        return tableView
    }

    var longFormHeight: PanModalHeight {
        return .maxHeightWithTopInset(100)
    }
    
//    var shortFormHeight: PanModalHeight {    
//        return .contentHeightIgnoringSafeArea(500)
//    }
    
    var allowsDragToDismiss: Bool {
        return true
    }

    var allowsTapToDismiss: Bool {
        return true
    }
    
    var cornerRadius: CGFloat {
        return 16
    }
    
    var panModalBackgroundColor: UIColor {
        return .black.withAlphaComponent(0.4)
    }
    
    var showDragIndicator: Bool {
        return false
    }
    
    func panModalWillDismiss() {
        presenter.panModalDismiss()
    }
}
