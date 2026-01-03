//
//  ComplainViewController.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import UIKit
import ComponentKit
import PanModal
import IQKeyboardManagerSwift

final class ComplainViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: ComplainPresenterProtocol {
        get { return self.basePresenter as! ComplainPresenterProtocol }
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
        let label = CKLabel(text: "Şikayet Et", textColor: .black, textAlignment: .center , font: .heading06)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var textView: CKCustomTextView = {
        let textView = CKCustomTextView(customDelegate: self,
                                        textColor: DesignKitColorName.contentStrong900.color,
                                        placeholder: "Açıklama",
                                        placeholderColor: DesignKitColorName.contentSoft600.color,
                                        font: .body04Compact,
                                        backgroundColor: DesignKitColorName.backgroundWeak100.color,
                                        borderWidth: 1,
                                        borderColor: DesignKitColorName.borderSoft200.color,
                                        cornerRadius: 8,
                                        padding: 16)
        textView.translatesAutoresizingMaskIntoConstraints = false
        return textView
    }()
    
    private lazy var submitButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Şikayet Et",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact,
            tag: 1)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    private var keyboardHeight: CGFloat = 0
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        IQKeyboardManager.shared.enable = false
        IQKeyboardManager.shared.enableAutoToolbar = false

        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(notification:)),
                                               name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide(notification:)),
                                               name: UIResponder.keyboardWillHideNotification, object: nil)
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.enableAutoToolbar = true
        NotificationCenter.default.removeObserver(self)
    }
    
    // MARK: - Custom Methods
    @objc private func keyboardWillShow(notification: Notification) {
        guard let userInfo = notification.userInfo,
              let keyboardFrame = userInfo[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect else { return }

        let height = keyboardFrame.height
        if keyboardHeight != height {
            keyboardHeight = height
            self.panModalSetNeedsLayoutUpdate()
            self.panModalTransition(to: .longForm)
        }
    }

    @objc private func keyboardWillHide(notification: Notification) {
        if keyboardHeight != 0 {
            keyboardHeight = 0
            self.panModalSetNeedsLayoutUpdate()
            self.panModalTransition(to: .longForm)
        }
    }
}

// MARK: - ComplainPresenterDelegate
extension ComplainViewController: ComplainPresenterDelegate {
    func prepareUI() {
        view.addSubview(scrollLineView)
        view.addSubview(titleLabel)
        view.addSubview(textView)
        view.addSubview(submitButton)
        
        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            titleLabel.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 16),
            titleLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            
            textView.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 16),
            textView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            textView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            textView.heightAnchor.constraint(equalToConstant: 140),
            
            submitButton.topAnchor.constraint(equalTo: textView.bottomAnchor, constant: 16),
            submitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            submitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16)
        ])
    }
}


// MARK: - PanModalPresentable
extension ComplainViewController: PanModalPresentable {
    var panScrollable: UIScrollView? {
        return nil
    }
    
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
    
    var longFormHeight: PanModalHeight {
        let topSpacing: CGFloat = 16 + 4 + 16
        let titleHeight: CGFloat = 20
        let textViewHeight: CGFloat = 140
        let buttonHeight: CGFloat = 46
        let verticalSpacing: CGFloat = 16 + 16 + 24
        let baseHeight = topSpacing + titleHeight + textViewHeight + buttonHeight + verticalSpacing
        if keyboardHeight > 0 {
            return .contentHeight(baseHeight + keyboardHeight)
        }

        return .contentHeight(baseHeight)
    }
}

// MARK: - CKCustomTextViewDelegate
extension ComplainViewController: CKCustomTextViewDelegate {
    func textViewDidChange(_ ckCustomTextView: CKCustomTextView) {
        UIView.performWithoutAnimation {
            ckCustomTextView.invalidateIntrinsicContentSize()
        }
        presenter.reasonDidChange(ckCustomTextView.text)
    }
    
    func textViewDidEndEditing(_ ckCustomTextView: CKCustomTextView) {
        presenter.reasonDidChange(ckCustomTextView.text)
    }
}

// MARK: - CKButtonDelegate
extension ComplainViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedButton(tag)
    }
}
