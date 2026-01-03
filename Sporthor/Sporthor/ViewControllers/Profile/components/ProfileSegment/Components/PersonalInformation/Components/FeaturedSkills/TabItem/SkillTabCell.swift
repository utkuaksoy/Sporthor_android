//
//  SkillTabCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class SkillTabCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.layer.cornerRadius = 18
        view.layer.borderWidth = 1
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var stackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.alignment = .center
        stackView.distribution = .fill
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentStrong900.color,
            numberOfLines: 1,
            textAlignment: .center,
            lineBreakMode: .byTruncatingTail,
            font: .bold04Compact
        )
        label.setContentCompressionResistancePriority(.required, for: .horizontal)
        label.setContentHuggingPriority(.required, for: .horizontal)
        return label
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(stackView)
        stackView.addArrangedSubview(iconImageView)
        stackView.addArrangedSubview(titleLabel)
        
        NSLayoutConstraint.activate([
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            stackView.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 8),
            stackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -16),
            stackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor, constant: -8),
            
            iconImageView.widthAnchor.constraint(equalToConstant: 16),
            iconImageView.heightAnchor.constraint(equalToConstant: 16)
        ])
    }
    
    func configure(with skill: SportSkillModel, isSelected: Bool) {
        titleLabel.text = skill.name
        if let icon = skill.icon {
            iconImageView.setImage(with: icon)
        }
        
        if isSelected {
            titleLabel.textColor = .white
            iconImageView.tintColor = .white
            containerView.backgroundColor = ColorName.backgroundPrimaryGreen.color
            containerView.layer.borderColor = ColorName.backgroundPrimaryGreen.color.cgColor
        } else {
            titleLabel.textColor = ColorName.contentSoft600.color
            iconImageView.tintColor = ColorName.contentSoft600.color
            containerView.backgroundColor = .white
            containerView.layer.borderColor = ColorName.borderSoft200.color.cgColor
        }
    }
}
