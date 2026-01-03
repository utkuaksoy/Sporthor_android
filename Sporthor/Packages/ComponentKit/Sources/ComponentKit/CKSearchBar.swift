//
//  CKSearchBar.swift
//  ComponentKit
//
//  Created by derTurke on 8.03.2025.
//

import UIKit

public class CKSearchBar: UIView {
    
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, spacing: 16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var textField: CKTextField = {
        let textField = CKTextField(delegate: self)
        return textField
    }()
    
    private lazy var clearButton: CKButton = {
        let button = CKButton(delegate: self, tag: 0)
        return button
    }()
    
    private lazy var cancelButton: CKButton = {
        let button = CKButton(delegate: self, tag: 1)
        return button
    }()
    
    // MARK: - Members
    private weak var delegate: CKSearchBarDelegate?
    private var cancelButtonTitle: String = ""
    
    public init(delegate: CKSearchBarDelegate? = nil,
                textColor: UIColor = .clear,
                placeholder: String = "",
                placeholderColor: UIColor = .clear,
                backgroundColor: UIColor = .clear,
                cornerRadius: CGFloat = 0,
                borderWidth: CGFloat = 0,
                borderColor: UIColor = .clear,
                selectedBorderColor: UIColor = .clear,
                font: UIFont? = .systemFont(ofSize: 14),
                image: UIImage? = nil,
                imagePosition: CKTextFieldImagePosition = .left,
                imageWidth: CGFloat = 18,
                imageHeight: CGFloat = 18,
                clearImage: UIImage? = nil,
                clearImageWidth: CGFloat = 18,
                clearImageHeight: CGFloat = 18,
                cancelButtonTitle: String = "",
                cancelButtonTitleColor: UIColor = .clear,
                cancelButtonFont: UIFont? = .systemFont(ofSize: 14),
                isHiddenCancelButton: Bool = true) {
        super.init(frame: .zero)
        self.delegate = delegate
        self.cancelButtonTitle = cancelButtonTitle
        textField.textColor = textColor
        textField.updatePlaceholderColor(placeholderColor)
        textField.updatePlaceholder(placeholder: placeholder)
        textField.backgroundColor = backgroundColor
        textField.setCornerRadius(cornerRadius)
        textField.setBorderWidth(borderWidth)
        textField.updateBorderColor(borderColor)
        textField.setSelectedBorderColor(selectedBorderColor)
        textField.font = font
        textField.setImagePosition(imagePosition)
        textField.updateImage(image: image, imageWidth: imageWidth, imageHeight: imageHeight)
        if let clearImage {
            clearButton.setImage(clearImage)
            clearButton.frame = CGRect(x: 0,
                                       y: 0,
                                       width: clearImageWidth,
                                       height: clearImageHeight)
            textField.rightView = clearButton
            textField.rightViewMode = .never
        }
        
        if !cancelButtonTitle.isEmpty {
            cancelButton.setTitle(cancelButtonTitle)
            cancelButton.setTitleColor(cancelButtonTitleColor)
            cancelButton.setFont(font)
            cancelButton.isHidden = isHiddenCancelButton
        } else {
            cancelButton.isHidden = true
        }
        
        textField.returnKeyType = .search
        
        setupView()
    }
    
    public required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        stackView.addArrangedSubviews([textField, cancelButton])
        addSubview(stackView)
        textField.setContentHuggingPriority(.defaultLow, for: .horizontal)
        textField.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
        cancelButton.setContentHuggingPriority(.required, for: .horizontal)
        cancelButton.setContentCompressionResistancePriority(.required, for: .horizontal)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    public func changeText(_ text: String) {
        textField.text = text
    }
}

extension CKSearchBar: CKTextFieldDelegate {
    nonisolated public func textFieldDidBeginEditing(_ textField: CKTextField) {
        Task {
            @MainActor in
            cancelButton.isHidden = cancelButtonTitle.isEmpty ? true : false
            delegate?.searchBarDidBeginEditing(self)
        }
    }
    
    nonisolated public func textFieldDidChangeSelection(_ textField: CKTextField) {
        Task {
            @MainActor in
            guard let text = textField.text else { return }
            textField.rightViewMode = text.isEmpty ? .never : .always
            delegate?.searchBarTextDidChange(self, text: text)
        }
    }
    
    nonisolated public func textFieldDidEndEditing(_ textField: CKTextField) {
        Task {
            @MainActor in
            guard let text = textField.text else { return }
            textField.rightViewMode = .never
            cancelButton.isHidden = true
            textField.resignFirstResponder()
            delegate?.searchBarTextDidEndEditing(self, text: text)
        }
    }
}

extension CKSearchBar: CKButtonDelegate {
    nonisolated public func ckButtonDidTap(tag: Int) {
        Task {
            @MainActor in
            switch tag {
            case 0:
                textField.text = ""
                textField.rightViewMode = .never
                delegate?.searchBarTextDidChange(self, text: "")
            case 1:
                cancelButton.isHidden = true
                textField.text = ""
                textField.resignFirstResponder()
                delegate?.searchBarDidCancel(self)
            default:
                break
            }
        }
    }
}
