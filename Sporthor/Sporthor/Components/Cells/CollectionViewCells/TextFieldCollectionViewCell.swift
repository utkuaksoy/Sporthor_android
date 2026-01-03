//
//  TextFieldCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke.
//

import UIKit
import ComponentKit

protocol TextFieldCollectionViewCellDelegate: AnyObject {
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?)
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath?)
    func textFieldImageTapped(tag: Int, indexPath: IndexPath?)
    func textField(_ textField: CKTextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool
}

extension TextFieldCollectionViewCellDelegate {
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {}
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath?) {}
    func textFieldImageTapped(tag: Int, indexPath: IndexPath?) {}
    func textField(_ textField: CKTextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool { return true }
}

final class TextFieldCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(alignment: .leading, spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(numberOfLines: 0)
        return label
    }()
    
    lazy var textField: CKTextField = {
        let textField = CKTextField(delegate: self)
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.heightAnchor.constraint(equalToConstant: 48).isActive = true
        textField.textContentType = .oneTimeCode
        return textField
    }()
    
    private var statusStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var statusTitleLabel: CKLabel = {
        let label = CKLabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var statusDescriptionStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .leading, spacing: 4)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var statusImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleToFill
        return imageView
    }()
    
    private lazy var statusDescriptionLabel: CKLabel = {
        let label = CKLabel(numberOfLines: 0)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private var statusImageViewHeightCons: NSLayoutConstraint!
    private var statusImageViewWidthCons: NSLayoutConstraint!
    
    // MARK: - Members
    private weak var delegate: TextFieldCollectionViewCellDelegate?
    private var indexPath: IndexPath?
    
    
    // MARK: - Initialize
    override init(frame: CGRect) {
        super.init(frame: frame)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        statusDescriptionStackView.addArrangedSubviews([statusImageView,
                                                        statusDescriptionLabel])
        statusStackView.addArrangedSubviews([statusTitleLabel, statusDescriptionStackView])
        stackView.addArrangedSubviews([titleLabel, textField, statusStackView])
        contentView.addSubview(stackView)
        statusImageViewWidthCons = statusImageView.widthAnchor.constraint(equalToConstant: 18)
        statusImageViewHeightCons = statusImageView.heightAnchor.constraint(equalToConstant: 18)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            textField.widthAnchor.constraint(equalTo: contentView.widthAnchor),
            statusImageViewWidthCons,
            statusImageViewHeightCons,
//            statusImageView.centerYAnchor.constraint(equalTo: statusStackView.centerYAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: TextFieldCollectionViewCellDelegate? = nil,
              titleText: String = "",
              titleColor: UIColor = DesignKitColorName.contentSub800.color,
              titleFont: UIFont? = .bold04Compact,
              textFieldText: String = "",
              textFieldPlaceholder: String = "",
              textFieldBorderColor: UIColor = .clear,
              textFieldTextAlignment: NSTextAlignment = .natural,
              textFieldFont: UIFont? = .body04Compact,
              textFieldImage: UIImage? = nil,
              textFieldImagePosition: CKTextFieldImagePosition = .right,
              textFieldImageWidth: CGFloat = 0,
              textFieldImageHeight: CGFloat = 0,
              textFieldIsEnabled: Bool = true,
              textFieldKeyboardType: UIKeyboardType = .default,
              textFieldCapitalizationType: UITextAutocapitalizationType = .sentences,
              textFieldIsSecureText: Bool = false,
              textFieldMaxLength: Int = 50,
              textFieldTag: Int = 0,
              statusTitleText: String = "",
              statusTitleColor: UIColor = DesignKitColorName.contentStrong900.color,
              statusTitleFont: UIFont? = .body04Compact,
              statusImage: UIImage? = nil,
              statusImageWidth: CGFloat = 18.0,
              statusImageHeight: CGFloat = 18.0,
              statusDescriptionText: String = "",
              statusDescriptionColor: UIColor = DesignKitColorName.contentStrong900.color,
              statusDescriptionFont: UIFont? = .body04Compact,
              indexPath: IndexPath? = nil) {
        
        self.delegate = delegate
        titleLabel.text = titleText
        titleLabel.textColor = titleColor
        titleLabel.font = titleFont
        
        textField.text = textFieldText
        textField.updatePlaceholder(placeholder: textFieldPlaceholder)
        textField.layer.borderColor = textFieldBorderColor.cgColor
        textField.textAlignment = textFieldTextAlignment
        textField.font = textFieldFont
        textField.updateImage(image: textFieldImage,
                              imageWidth: textFieldImageWidth,
                              imageHeight: textFieldImageHeight)
        textField.isEnabled = textFieldIsEnabled
        textField.keyboardType = textFieldKeyboardType
        textField.autocapitalizationType = textFieldCapitalizationType
        textField.isSecureTextEntry = textFieldIsSecureText
        textField.setMaxLength(textFieldMaxLength)
        textField.tag = textFieldTag
        
        // Set UITextFieldDelegate if delegate conforms to it
        if let textFieldDelegate = delegate as? UITextFieldDelegate {
            textField.delegate = textFieldDelegate
        }
        
        statusTitleLabel.text = statusTitleText
        statusTitleLabel.textColor = statusTitleColor
        statusTitleLabel.font = statusTitleFont
        
        statusImageView.image = statusImage
        statusImageViewWidthCons.isActive = false
        statusImageViewHeightCons.isActive = false
        
        statusImageViewWidthCons = statusImageView.widthAnchor.constraint(equalToConstant: statusImageWidth)
        statusImageViewHeightCons = statusImageView.heightAnchor.constraint(equalToConstant: statusImageHeight)
        statusImageViewWidthCons.isActive = true
        statusImageViewHeightCons.isActive = true
        
        statusDescriptionLabel.text = statusDescriptionText
        statusDescriptionLabel.textColor = statusDescriptionColor
        statusDescriptionLabel.font = statusDescriptionFont
        statusStackView.isHidden = statusDescriptionText.isEmpty
        
        self.indexPath = indexPath
        layoutIfNeeded()
        contentView.layoutIfNeeded()
    }
    
    func textFieldBecomeFirstResponder() {
        textField.becomeFirstResponder()
    }
    
    func updateText(_ text: String) {
        textField.text = text
    }
    
    func getTextField() -> CKTextField {
        return textField
    }
}

// MARK: - CKTextFieldDelegate
extension TextFieldCollectionViewCell: CKTextFieldDelegate {
    func textFieldDidChangeSelection(_ textField: CKTextField) {
        guard let text = textField.text, let delegate else { return }
        delegate.textFieldDidChangeSelection(text: text, tag: textField.tag, indexPath: indexPath)
    }
    
    func textFieldDidEndEditing(_ textField: CKTextField) {
        guard let text = textField.text, let delegate else { return }
        delegate.textFieldDidEndEditing(text: text, tag: textField.tag, indexPath: indexPath)
    }
    
    func textFieldImageTapped(_ textField: CKTextField) {
        guard let delegate else { return }
        delegate.textFieldImageTapped(tag: textField.tag, indexPath: indexPath)
    }
    
    func textField(_ textField: CKTextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return delegate?.textField(textField, shouldChangeCharactersIn: range, replacementString: string) ?? true
    }
}
