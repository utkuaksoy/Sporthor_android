//
//  EmptyTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 30.04.2025.
//

import UIKit
import ComponentKit

final class EmptyTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var imageBackgroundView: UIView = {
        let view = UIView()
        view.setCornerRadius(36)
        view.backgroundColor = DesignKitColorName.contentWeak100.color
        view.translatesAutoresizingMaskIntoConstraints = false
        view.widthAnchor.constraint(equalToConstant: 72).isActive = true
        view.heightAnchor.constraint(equalToConstant: 72).isActive = true
        return view
    }()
    
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.clipsToBounds = true
        imageView.setCornerRadius(16)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 32).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 32).isActive = true
        return imageView
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSoft600.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .heading04)
        return label
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .vertical, alignment: .center, spacing: 16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    
    // MARK: - Initialize
    public override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
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
        imageBackgroundView.addSubview(iconImageView)
        stackView.addArrangedSubviews([imageBackgroundView, descriptionLabel])
        contentView.addSubview(stackView)
        
        NSLayoutConstraint.activate([
            iconImageView.centerXAnchor.constraint(equalTo: imageBackgroundView.centerXAnchor),
            iconImageView.centerYAnchor.constraint(equalTo: imageBackgroundView.centerYAnchor),
            
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 48),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(image: UIImage, description: String) {
        iconImageView.image = image
        descriptionLabel.text = description
    }
}
