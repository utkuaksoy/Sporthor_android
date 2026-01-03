//
//  TextFieldCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke.
//

import UIKit
import ComponentKit

protocol DatePickerCollectionViewCellDelegate: AnyObject {
    func selectedDatePicker(text: String, tag: Int, indexPath: IndexPath?)
}

extension DatePickerCollectionViewCellDelegate {
    func selectedDatePicker(text: String, tag: Int, indexPath: IndexPath?) {}
}

final class DatePickerCollectionViewCell: UICollectionViewCell {
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
    
    private lazy var datePicker: CKDatePicker = {
        let datePicker = CKDatePicker()
        datePicker.translatesAutoresizingMaskIntoConstraints = false
        datePicker.heightAnchor.constraint(equalToConstant: 48).isActive = true
        return datePicker
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
    private weak var delegate: DatePickerCollectionViewCellDelegate?
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
        stackView.addArrangedSubviews([titleLabel, datePicker, statusStackView])
        contentView.addSubview(stackView)
        statusImageViewWidthCons = statusImageView.widthAnchor.constraint(equalToConstant: 18)
        statusImageViewHeightCons = statusImageView.heightAnchor.constraint(equalToConstant: 18)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            datePicker.widthAnchor.constraint(equalTo: contentView.widthAnchor),
            statusImageViewWidthCons,
            statusImageViewHeightCons,
//            statusImageView.centerYAnchor.constraint(equalTo: statusStackView.centerYAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: DatePickerCollectionViewCellDelegate? = nil,
              type: CKPickerType,
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
        datePicker.ckDatePickerDelegate = self
        titleLabel.text = titleText
        titleLabel.textColor = titleColor
        titleLabel.font = titleFont
        
        datePicker.pickerType = type
        datePicker.text = textFieldText
        datePicker.updatePlaceholder(placeholder: textFieldPlaceholder)
        datePicker.layer.borderColor = textFieldBorderColor.cgColor
        datePicker.textAlignment = textFieldTextAlignment
        datePicker.font = textFieldFont
        datePicker.updateImage(image: textFieldImage,
                              imageWidth: textFieldImageWidth,
                              imageHeight: textFieldImageHeight)
        datePicker.isEnabled = textFieldIsEnabled
        datePicker.keyboardType = textFieldKeyboardType
        datePicker.autocapitalizationType = textFieldCapitalizationType
        datePicker.isSecureTextEntry = textFieldIsSecureText
        datePicker.setMaxLength(textFieldMaxLength)
        datePicker.tag = textFieldTag
        
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
        datePicker.becomeFirstResponder()
    }
    
    func updateText(_ text: String) {
        datePicker.text = text
    }
    
    func getTextField() -> CKTextField {
        return datePicker
    }
}

// MARK: - CKDatePickerDelegate
extension DatePickerCollectionViewCell: CKDatePickerDelegate {
    func selectedDate(_ date: Date, textField: CKTextField) {
        delegate?.selectedDatePicker(text: date.toString(), tag: textField.tag, indexPath: indexPath)
    }
    
    func selectedTime(_ time: Date, textField: CKTextField) {
        delegate?.selectedDatePicker(text: time.toString("HH:mm"), tag: textField.tag, indexPath: indexPath)
    }
    
    func selectedYear(_ year: String, textField: CKTextField) {
        delegate?.selectedDatePicker(text: year, tag: textField.tag, indexPath: indexPath)
    }
    
    func selectedMonth(_ month: String, textField: CKTextField) {
        delegate?.selectedDatePicker(text: month, tag: textField.tag, indexPath: indexPath)
    }
}
