//
//  SelectionCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//

import UIKit
import ComponentKit

protocol SelectionCollectionViewCellDelegate: AnyObject {
    func didTappedSelectionCollectionViewCell(tag: Int)
}

extension SelectionCollectionViewCellDelegate {
    func didTappedSelectionCollectionViewCell(tag: Int) {}
}

final class SelectionCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var textField: CKTextField = {
        let textField = CKTextField()
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    // MARK: - Members
    private weak var delegate: SelectionCollectionViewCellDelegate?

    // MARK: - Initilaizers
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupUI()
    }
    
    private func setupUI() {
        contentView.addSubview(textField)
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedCell))
        self.isUserInteractionEnabled = true
        self.addGestureRecognizer(tapGesture)
        
        NSLayoutConstraint.activate([
            textField.topAnchor.constraint(equalTo: contentView.topAnchor),
            textField.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            textField.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            textField.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: SelectionCollectionViewCellDelegate? = nil,
              text: String = "",
              textColor: UIColor = DesignKitColorName.contentStrong900.color,
              placeholder: String = "Seçiniz",
              placeholderColor: UIColor = DesignKitColorName.contentSoft600.color,
              cornerRadius: CGFloat = 8,
              borderWidth: CGFloat = 1,
              borderColor: UIColor = .clear,
              font: UIFont? = .body04Compact,
              image: UIImage? = Asset.chevronDown.image,
              imagePosition: CKTextFieldImagePosition = .right,
              imageWidth: CGFloat = 24,
              imageHeight: CGFloat = 24,
              isEnabled: Bool = false,
              tag: Int = 0) {
        self.delegate = delegate
        textField.text = text
        textField.textColor = textColor
        textField.updatePlaceholderColor(placeholderColor)
        textField.updatePlaceholder(placeholder: placeholder)
        textField.setCornerRadius(cornerRadius)
        textField.setBorderWidth(borderWidth)
        textField.setBorderColor(borderColor)
        textField.font = font
        textField.updateImage(image: image,
                              imageWidth: imageWidth,
                              imageHeight: imageHeight)
        textField.setImagePosition(imagePosition)
        textField.isEnabled = isEnabled
        self.tag = tag
    }
    
    @objc private func didTappedCell() {
        delegate?.didTappedSelectionCollectionViewCell(tag: self.tag)
    }
}
