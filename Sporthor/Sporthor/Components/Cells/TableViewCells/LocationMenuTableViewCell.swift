//
//  LocationMenuTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 5.06.2025.
//

import UIKit
import ComponentKit

protocol LocationMenuTableViewCellDelegate: AnyObject {
    func didTappedLocationMenu()
}

extension LocationMenuTableViewCellDelegate {
    func didTappedLocationMenu() {}
}

final class LocationMenuTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .body03Compact)
        return label
    }()
    
    private lazy var descriptionTitleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var descriptionStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.addArrangedSubviews([descriptionTitleLabel, descriptionLabel])
        return stackView
    }()
    
    private lazy var verticalStackView: CKStackView = {
        let stackView = CKStackView(spacing: 12)
        stackView.addArrangedSubviews([titleLabel, descriptionStackView])
        return stackView
    }()
    
    private lazy var rightIconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var contentStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    alignment: .center,
                                    spacing: 12)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([iconImageView,
                                       verticalStackView,
                                       rightIconImageView])
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: LocationMenuTableViewCellDelegate?
    
    // MARK: - Initializers
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        selectionStyle = .none
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        contentView.addSubview(contentStackView)
        
        
        NSLayoutConstraint.activate([
            contentStackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            contentStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            contentStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            contentStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
        ])
    }
    
    // MARK: - Custom Methods
    func bind(
        delegate: LocationMenuTableViewCellDelegate? = nil,
        leftIcon: UIImage? = nil,
        title: String = "",
        rightIcon: UIImage? = nil,
        descriptionTitle: String = "",
        description: String = ""
    ) {
        self.delegate = delegate
        iconImageView.image = leftIcon
        titleLabel.text = title
        rightIconImageView.image = rightIcon
        rightIconImageView.isHidden = (descriptionTitle.isEmpty && description.isEmpty)
        descriptionTitleLabel.text = descriptionTitle
        descriptionLabel.text = description
        descriptionStackView.isHidden = (descriptionTitle.isEmpty && description.isEmpty)
        contentStackView.alignment = (descriptionTitle.isEmpty && description.isEmpty) ? .center : .top
        
        contentView.isUserInteractionEnabled = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedLocationMenu))
        contentView.addGestureRecognizer(tapGesture)
    }
    
    @objc private func didTappedLocationMenu() {
        delegate?.didTappedLocationMenu()
    }
}
