//
//  CalendarAddEventTypeViewController.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import UIKit
import ComponentKit
import IQKeyboardManagerSwift
import PanModal

final class CalendarAddEventTypeViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CalendarAddEventTypePresenterProtocol {
        get { return self.basePresenter as! CalendarAddEventTypePresenterProtocol }
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
        let label = CKLabel(text: "Etkinlik Tipi Ekle", textColor: .black, textAlignment: .center , font: .heading06)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var textField: CKTextField = {
        let textField = CKTextField(delegate: self,
                                    placeholder: "Etkinlik Tipi",
                                    maxLength: 50)
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    private lazy var submitButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Ekle",
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

// MARK: - CalendarAddEventTypePresenterDelegate
extension CalendarAddEventTypeViewController: CalendarAddEventTypePresenterDelegate {
    func prepareUI() {
        view.addSubview(scrollLineView)
        view.addSubview(titleLabel)
        view.addSubview(textField)
        view.addSubview(submitButton)
        
        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            titleLabel.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 16),
            titleLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            
            textField.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 16),
            textField.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            textField.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            textField.heightAnchor.constraint(equalToConstant: 48),
            
            submitButton.topAnchor.constraint(equalTo: textField.bottomAnchor, constant: 64),
            submitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            submitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16)
        ])
    }
}

// MARK: - PanModalPresentable
extension CalendarAddEventTypeViewController: PanModalPresentable {
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
        let textFieldHeight: CGFloat = 48
        let buttonHeight: CGFloat = 46
        let verticalSpacing: CGFloat = 16 + 16 + 64
        let baseHeight = topSpacing + titleHeight + textFieldHeight + buttonHeight + verticalSpacing
        if keyboardHeight > 0 {
            return .contentHeight(baseHeight + keyboardHeight)
        }

        return .contentHeight(baseHeight)
    }
}

// MARK: - CKTextFieldDelegate
extension CalendarAddEventTypeViewController: CKTextFieldDelegate {
    func textFieldDidChangeSelection(_ textField: CKTextField) {
        guard let text = textField.text else { return }
        presenter.didEndEditingTextField(text)
    }
    
    func textFieldDidEndEditing(_ textField: CKTextField) {
        guard let text = textField.text else { return }
        presenter.didEndEditingTextField(text)
    }
}

// MARK: - CKButtonDelegate
extension CalendarAddEventTypeViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedSubmitButton()
    }
}


