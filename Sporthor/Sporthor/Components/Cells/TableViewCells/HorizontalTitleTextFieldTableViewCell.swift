//
//  HorizontalTitleTextFieldTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 9.04.2025.
//

import UIKit
import ComponentKit

protocol HorizontalTitleTextFieldTableViewCellDelegate: AnyObject {
    func horizontalTextFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath)
}


final class HorizontalTitleTextFieldTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    alignment: .fill,
                                    spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(numberOfLines: 0)
        return label
    }()
    
    private lazy var textFieldStackView: CKStackView = {
        let stackView = CKStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.widthAnchor.constraint(equalToConstant: 243).isActive = true
        return stackView
    }()
    
    private lazy var textField: CKTextField = {
        let textField = CKTextField(delegate: self)
        textField.translatesAutoresizingMaskIntoConstraints = false
        textField.heightAnchor.constraint(equalToConstant: 26).isActive = true
        
        return textField
    }()
    
    private lazy var textFieldSeperatorView: UIView = {
        let view = UIView()
        view.heightAnchor.constraint(equalToConstant: 1).isActive = true
        return view
    }()
    
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
        textFieldStackView.addArrangedSubviews([textField, textFieldSeperatorView])
        stackView.addArrangedSubviews([titleLabel, textFieldStackView])
        contentView.addSubview(stackView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -12)
        ])
    }
    
    // MARK: - Members
    private weak var delegate: HorizontalTitleTextFieldTableViewCellDelegate?
    private var indexPath: IndexPath?
    private var textFieldType: Int?
    
    // MARK: - Custom Methods
    func bind(delegate: HorizontalTitleTextFieldTableViewCellDelegate? = nil,
              titleText: String = "",
              titleColor: UIColor = DesignKitColorName.contentStrong900.color,
              titleFont: UIFont? = .body04Compact,
              textFieldText: String = "",
              textFieldPlaceholder: String = "",
              textFieldBackgroundColor: UIColor = .clear,
              textFieldFont: UIFont = .body04Compact,
              textFieldIsEnabled: Bool = true,
              textFieldKeyboardType: UIKeyboardType = .default,
              textFieldCapitalizationType: UITextAutocapitalizationType = .sentences,
              textFieldIsSecureText: Bool = false,
              textFieldMaxLength: Int = 50,
              textFieldTag: Int = 0,
              textFieldSeperatorColor: UIColor = DesignKitColorName.borderSoft200.color,
              textFieldType: Int? = nil,
              indexPath: IndexPath) {
        self.delegate = delegate
        titleLabel.text = titleText
        titleLabel.textColor = titleColor
        titleLabel.font = titleFont
        
        textField.text = textFieldText
        textField.updatePlaceholder(placeholder: textFieldPlaceholder)
        textField.backgroundColor = textFieldBackgroundColor
        textField.font = textFieldFont
        textField.isEnabled = textFieldIsEnabled
        textField.keyboardType = textFieldKeyboardType
        textField.autocapitalizationType = textFieldCapitalizationType
        textField.isSecureTextEntry = textFieldIsSecureText
        textField.setMaxLength(textFieldMaxLength)
        textField.tag = textFieldTag
        textFieldSeperatorView.backgroundColor = textFieldSeperatorColor
        textField.setSelectedBorderColor(.clear)
        textField.setPadding(0)
        if let textFieldType {
            self.textFieldType = textFieldType
            prepareTextFieldWithType()
        }
        
        self.indexPath = indexPath
    }
    
    private func prepareTextFieldWithType() {
        guard let textFieldType else { return }
        switch HorizontalTitleTextFieldType(rawValue: textFieldType) {
        case .normal:
            textField.keyboardType = .default
            textField.setMaxLength(255)
        case .birthday:
            textField.keyboardType = .numberPad
            textField.setMaxLength(10)
        case .centimeter, .kilogram, .second:
            textField.keyboardType = .decimalPad
            textField.setMaxLength(5)
        case .degree:
            textField.keyboardType = .decimalPad
            textField.setMaxLength(3)
        default:
            textField.keyboardType = .default
            textField.setMaxLength(255)
        }
    }
    
    func bindForRow(delegate: HorizontalTitleTextFieldTableViewCellDelegate? = nil,
                    model: ProfileSummaryTextFieldRow?,
                    indexPath: IndexPath) {
        guard let model = model else { return }
        bind(delegate: delegate,
             titleText: model.title ?? "",
             textFieldText: model.text ?? "",
             textFieldPlaceholder: model.placeholder ?? "",
             textFieldIsEnabled: model.isEnabled ?? true,
             textFieldType: model.type,
             indexPath: indexPath)
    }
    
    func updateEnabled(_ isEnabled: Bool) {
        textField.isEnabled = isEnabled
    }
}

extension HorizontalTitleTextFieldTableViewCell: CKTextFieldDelegate {
    func textFieldDidBeginEditing(_ textField: CKTextField) {
        guard let text = textField.text,
              let rawType = textFieldType,
              let type = HorizontalTitleTextFieldType(rawValue: rawType) else { return }
        
        textField.text = type.strippedText(from: text)
    }
    
    func textFieldDidChangeSelection(_ textField: CKTextField) {
        guard let text = textField.text,
              let rawType = self.textFieldType,
              let type = HorizontalTitleTextFieldType(rawValue: rawType) else { return }
        switch type {
        case .birthday:
            let selectedRange = textField.selectedTextRange
            let oldCursorPosition = textField.offset(from: textField.beginningOfDocument, to: selectedRange?.start ?? UITextPosition())
            
            let unformattedText = text.replacingOccurrences(of: ".", with: "")
            let newText = unformattedText.formatDateField()
            
            var newCursorPosition = oldCursorPosition
            if newText.count > text.count {
                newCursorPosition += 1
            }
            
            textField.text = newText
            
            if let newPosition = textField.position(from: textField.beginningOfDocument, offset: min(newCursorPosition, newText.count)) {
                textField.selectedTextRange = textField.textRange(from: newPosition, to: newPosition)
            }
        default:
            break
        }
    }
    
    func textFieldDidEndEditing(_ textField: CKTextField) {
        guard var text = textField.text,
              let delegate,
              let indexPath = indexPath else { return }
        
        if let rawType = textFieldType,
           let type = HorizontalTitleTextFieldType(rawValue: rawType),
           !text.isEmpty {
            text = type.formattedText(from: text)
            textField.text = text
        }
        
        delegate.horizontalTextFieldDidEndEditing(text: text, tag: textField.tag, indexPath: indexPath)
    }
}

enum HorizontalTitleTextFieldType: Int {
    case normal = 0
    case birthday
    case centimeter
    case kilogram
    case second
    case degree
}

extension HorizontalTitleTextFieldType {
    var unitSuffix: String {
        switch self {
        case .centimeter: return " cm"
        case .kilogram: return " kg"
        case .second: return " sn"
        case .degree: return "°"
        default: return ""
        }
    }
    
    func strippedText(from text: String) -> String {
        guard !unitSuffix.isEmpty else { return text }
        return text.replacingOccurrences(of: unitSuffix, with: "").trimmingCharacters(in: .whitespaces)
    }
    
    func formattedText(from text: String) -> String {
        guard !unitSuffix.isEmpty else { return text }
        return strippedText(from: text) + unitSuffix
    }
}
