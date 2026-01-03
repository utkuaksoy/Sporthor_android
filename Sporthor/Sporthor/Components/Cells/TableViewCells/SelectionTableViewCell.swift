//
//  SelectionTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 15.04.2025.
//

import UIKit
import ComponentKit

final class SelectionTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var valueLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Constraints
    private var iconHeightConstraint: NSLayoutConstraint!
    private var iconWidthConstraint: NSLayoutConstraint!
    private var valueLabelLeadingWithIconConstraint: NSLayoutConstraint!
    private var valueLabelLeadingWithoutIconConstraint: NSLayoutConstraint!
    
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
        contentView.addSubview(iconImageView)
        contentView.addSubview(valueLabel)
        
        iconHeightConstraint = iconImageView.heightAnchor.constraint(equalToConstant: 24)
        iconWidthConstraint = iconImageView.widthAnchor.constraint(equalToConstant: 24)
        
        valueLabelLeadingWithIconConstraint = valueLabel.leadingAnchor.constraint(equalTo: iconImageView.trailingAnchor, constant: 8)
        valueLabelLeadingWithoutIconConstraint = valueLabel.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16)
        
        NSLayoutConstraint.activate([
            iconImageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            iconImageView.centerYAnchor.constraint(equalTo: valueLabel.centerYAnchor),
            iconHeightConstraint,
            iconWidthConstraint,
            
            valueLabel.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            valueLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            valueLabel.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            
            valueLabelLeadingWithIconConstraint
        ])
    }
    
    // MARK: - Custom Methods
    func bind(with model: SelectionModel) {
        valueLabel.text = model.value
        
        if let image = model.image, !image.isEmpty {
            iconImageView.isHidden = false
            iconImageView.setImage(with: image)
            iconHeightConstraint.constant = 24
            iconWidthConstraint.constant = 24
            
            valueLabelLeadingWithoutIconConstraint.isActive = false
            valueLabelLeadingWithIconConstraint.isActive = true
        } else {
            iconImageView.isHidden = true
            iconHeightConstraint.constant = 0
            iconWidthConstraint.constant = 0
            
            valueLabelLeadingWithIconConstraint.isActive = false
            valueLabelLeadingWithoutIconConstraint.isActive = true
        }
    }
}
