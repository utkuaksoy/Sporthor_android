//
//  PhoneCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke.
//

import UIKit
import ComponentKit

protocol PhoneCollectionViewCellDelegate: AnyObject {
    func didChangeText(_ text: String, at indexPath: IndexPath?, tag: Int)
}

extension PhoneCollectionViewCellDelegate {
    func didChangeText(_ text: String, at indexPath: IndexPath?, tag: Int) {}
}

final class PhoneCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var containerStackView: CKStackView = {
        let stackView = CKStackView(spacing: 16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color, font: .bold04Compact)
        return label
    }()
    
    private lazy var textFieldStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, spacing: 16)
        return stackView
    }()
    
    private lazy var areaTextField: CKTextField = {
        let textField = CKTextField(
            delegate: self,
            font: .bold03Compact,
            imagePosition: .left,
            isEnabled: false,
            tag: 0)
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    private lazy var phoneTextField: CKTextField = {
        let textField = CKTextField(
            delegate: self,
            keyboardType: .numberPad,
            maxLength: 10,
            tag: 1)
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    // MARK: - Members
    private weak var delegate: PhoneCollectionViewCellDelegate?
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
        textFieldStackView.addArrangedSubviews([areaTextField, phoneTextField])
        containerStackView.addArrangedSubviews([titleLabel, textFieldStackView])
        contentView.addSubview(containerStackView)
        
        NSLayoutConstraint.activate([
            containerStackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            areaTextField.widthAnchor.constraint(equalToConstant: 100),
            areaTextField.heightAnchor.constraint(equalToConstant: 48)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: PhoneCollectionViewCellDelegate? = nil,
              indexPath: IndexPath,
              title: String = "",
              areaText: String = "",
              areaImage: UIImage? = nil,
              phoneText: String = "",
              phonePlaceholder: String = "") {
        self.delegate = delegate
        self.indexPath = indexPath
        titleLabel.text = title
        areaTextField.text = areaText
        if !areaText.isEmpty {
            delegate?.didChangeText(areaText, at: indexPath, tag: areaTextField.tag)
        }
        areaTextField.updateImage(image: areaImage, imageWidth: 24, imageHeight: 24)
        phoneTextField.text = phoneText
        phoneTextField.updatePlaceholder(placeholder: phonePlaceholder)
    }
}


extension PhoneCollectionViewCell: CKTextFieldDelegate {
    func textFieldDidBeginEditing(_ textField: CKTextField) {
        switch textField.tag {
        case 1:
            textField.text = textField.text?.removePhoneNumberFormatting()
        default:
            break
        }
    }
    
    func textFieldDidEndEditing(_ textField: CKTextField) {
        guard let text = textField.text else { return }
        switch textField.tag {
        case 1:
            textField.text = text.formatPhoneNumber()
        default:
            break
        }
        delegate?.didChangeText(text, at: indexPath, tag: textField.tag)
    }
}
