//
//  TextFieldTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 4.03.2025.
//

import UIKit
import ComponentKit

protocol TextFieldTableViewCellDelegate: AnyObject {
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?)
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath?)
    func textFieldImageTapped(tag: Int, indexPath: IndexPath?)
}

extension TextFieldTableViewCellDelegate {
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {}
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath?) {}
    func textFieldImageTapped(tag: Int, indexPath: IndexPath?) {}
}

final class TextFieldTableViewCell: UITableViewCell {
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
    
    private lazy var textField: CKTextField = {
        let textField = CKTextField(delegate: self)
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.heightAnchor.constraint(equalToConstant: 48).isActive = true
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
    
    private var leadingAnchorCons: NSLayoutConstraint!
    private var trailingAnchorCons: NSLayoutConstraint!
    private var textFieldLeadingAnchorCons: NSLayoutConstraint!
    private var textFieldTrailingAnchorCons: NSLayoutConstraint!
    
    // MARK: - Members
    private weak var delegate: TextFieldTableViewCellDelegate?
    private var indexPath: IndexPath?
    
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        statusDescriptionStackView.addArrangedSubviews([statusImageView,
                                                        statusDescriptionLabel])
        statusStackView.addArrangedSubviews([statusTitleLabel, statusDescriptionStackView])
        stackView.addArrangedSubviews([titleLabel, textField, statusStackView])
        contentView.addSubview(stackView)
        
        statusImageViewWidthCons = statusImageView.widthAnchor.constraint(equalToConstant: 18)
        statusImageViewHeightCons = statusImageView.heightAnchor.constraint(equalToConstant: 18)
        
        leadingAnchorCons = stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor)
        trailingAnchorCons = stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor)
        textFieldLeadingAnchorCons = textField.leadingAnchor.constraint(equalTo: contentView.leadingAnchor)
        textFieldTrailingAnchorCons = textField.trailingAnchor.constraint(equalTo: contentView.trailingAnchor)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            leadingAnchorCons,
            trailingAnchorCons,
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            textFieldLeadingAnchorCons,
            textFieldTrailingAnchorCons,
            statusImageViewWidthCons,
            statusImageViewHeightCons,
            //            statusImageView.centerYAnchor.constraint(equalTo: statusStackView.centerYAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: TextFieldTableViewCellDelegate? = nil,
              titleText: String = "",
              titleColor: UIColor = DesignKitColorName.contentSub800.color,
              titleFont: UIFont? = .bold04Compact,
              textFieldText: String = "",
              textFieldPlaceholder: String = "",
              textFieldPlaceholderColor: UIColor = DesignKitColorName.contentSoft600.color,
              textFieldBackgroundColor: UIColor = DesignKitColorName.backgroundWeak100.color,
              textFieldBorderColor: UIColor = .clear,
              textFieldSelectedBorderColor: UIColor = DesignKitColorName.borderStrong900.color,
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
              indexPath: IndexPath? = nil,
              leadingAnchorCons: CGFloat = 0,
              trailingAnchorCons: CGFloat = 0) {
        self.delegate = delegate
        titleLabel.text = titleText
        titleLabel.textColor = titleColor
        titleLabel.font = titleFont
        
        textField.backgroundColor = textFieldBackgroundColor
        textField.text = textFieldText
        textField.updatePlaceholderColor(textFieldPlaceholderColor)
        textField.updatePlaceholder(placeholder: textFieldPlaceholder)
        textField.layer.borderColor = textFieldBorderColor.cgColor
        textField.setSelectedBorderColor(textFieldBorderColor)
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
        
        NSLayoutConstraint.deactivate([
            self.leadingAnchorCons,
            self.trailingAnchorCons,
            self.textFieldLeadingAnchorCons,
            self.textFieldTrailingAnchorCons,
        ])
        
        self.leadingAnchorCons = stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor,
                                                                    constant: leadingAnchorCons)
        self.trailingAnchorCons = stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor,
                                                                      constant: trailingAnchorCons)
        
        self.textFieldLeadingAnchorCons = textField.leadingAnchor.constraint(equalTo: contentView.leadingAnchor,
                                                                             constant: leadingAnchorCons)
        self.textFieldTrailingAnchorCons = textField.trailingAnchor.constraint(equalTo: contentView.trailingAnchor,
                                                                               constant: trailingAnchorCons)
        self.leadingAnchorCons.isActive = true
        self.trailingAnchorCons.isActive = true
        self.textFieldLeadingAnchorCons.isActive = true
        self.textFieldTrailingAnchorCons.isActive = true
        
        layoutIfNeeded()
        contentView.layoutIfNeeded()
    }
    
    func textFieldBecomeFirstResponder() {
        textField.becomeFirstResponder()
    }
}

// MARK: - CKTextFieldDelegate
extension TextFieldTableViewCell: CKTextFieldDelegate {
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
}
