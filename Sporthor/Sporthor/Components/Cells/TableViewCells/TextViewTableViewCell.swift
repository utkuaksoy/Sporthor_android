//
//  TextViewTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 23.04.2025.
//

import UIKit
import ComponentKit

protocol TextViewTableViewCellDelegate: AnyObject {
    func textViewDidEndEditing(_ text: String, tag: Int)
    func textViewDidChange(_ text: String, tag: Int)
}

extension TextViewTableViewCellDelegate {
    func textViewDidEndEditing(_ text: String, tag: Int) {}
    func textViewDidChange(_ text: String, tag: Int) {}
}

final class TextViewTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var textView: CKCustomTextView = {
        let textView = CKCustomTextView(padding: 16)
        textView.translatesAutoresizingMaskIntoConstraints = false
        return textView
    }()
    
    // MARK: - Members
    private weak var delegate: TextViewTableViewCellDelegate?
    
    // MARK: - Initializers
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
        contentView.addSubview(textView)
        
        NSLayoutConstraint.activate([
            textView.topAnchor.constraint(equalTo: contentView.topAnchor),
            textView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            textView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            textView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: TextViewTableViewCellDelegate? = nil,
              text: String = "",
              textColor: UIColor = .clear,
              placeholder: String = "",
              placeholderColor: UIColor = .clear,
              font: UIFont = .systemFont(ofSize: 14),
              backgroundColor: UIColor = .clear,
              borderWidth: CGFloat = 0,
              borderColor: UIColor = .clear,
              selectedBorderColor: UIColor = .clear,
              padding: CGFloat = 0,
              maxLength: Int? = nil,
              minHeight: CGFloat? = nil,
              maxHeight: CGFloat? = nil,
              scrollEnabled: Bool = true,
              tag: Int = 0) {
        self.delegate = delegate
        textView.bind(customDelegate: self,
                      text: text,
                      textColor: textColor,
                      placeholder: placeholder,
                      placeholderColor: placeholderColor,
                      font: font,
                      borderWidth: borderWidth,
                      borderColor: borderColor,
                      selectedBorderColor: selectedBorderColor,
                      padding: padding,
                      maxLength: maxLength,
                      tag: tag,
                      minHeight: minHeight,
                      maxHeight: maxHeight,
                      scrollEnabled: scrollEnabled)
    }
}

extension TextViewTableViewCell: CKCustomTextViewDelegate {
    func textViewDidEndEditing(_ ckCustomTextView: CKCustomTextView) {
        UIView.performWithoutAnimation {
            textView.invalidateIntrinsicContentSize()
            layoutIfNeeded()
        }
        
        guard let delegate else { return }
        delegate.textViewDidEndEditing(ckCustomTextView.text,
                                       tag: ckCustomTextView.tag)
    }
    
    func textViewDidChange(_ ckCustomTextView: CKCustomTextView) {
        guard let delegate else { return }
        delegate.textViewDidChange(ckCustomTextView.text,
                                   tag: ckCustomTextView.tag)
    }
}
