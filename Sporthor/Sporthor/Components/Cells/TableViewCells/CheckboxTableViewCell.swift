//
//  CheckboxTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//

import UIKit
import ComponentKit

protocol CheckboxTableViewCellDelegate: AnyObject {
    func selectedCheckboxTableViewCell(isSelected: Bool, tag: Int)
}

extension CheckboxTableViewCellDelegate {
    func selectedCheckboxTableViewCell(isSelected: Bool, tag: Int) {}
}

final class CheckboxTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.heightAnchor.constraint(equalToConstant: 22).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 22).isActive = true
        imageView.isUserInteractionEnabled = true
        imageView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didSelectCheckbox)))
        return imageView
    }()
    
    private lazy var infoLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.addArrangedSubviews([iconImageView, infoLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: CheckboxTableViewCellDelegate?
    private var selectedCheckbox: Bool = false
    
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
        selectionStyle = .none
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        contentView.addSubview(stackView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16)
        ])
    }
    
    // MARK: - Custom Methods
    func configure(delegate: CheckboxTableViewCellDelegate? = nil,
                   info: String,
                   isSelected: Bool = false,
                   tag: Int = 0) {
        self.delegate = delegate
        infoLabel.text = info
        self.selectedCheckbox = isSelected
        iconImageView.image = isSelected ? Asset.selectedCheckbox.image : Asset.checkbox.image
        self.tag = tag
    }
    
    @objc private func didSelectCheckbox() {
        delegate?.selectedCheckboxTableViewCell(isSelected: !self.selectedCheckbox, tag: self.tag)
    }
}
