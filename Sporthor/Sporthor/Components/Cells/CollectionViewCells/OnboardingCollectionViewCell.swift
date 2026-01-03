//
//  OnboardingCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//

import UIKit
import ComponentKit

final class OnboardingCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .heading04)
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body03Compact)
        return label
    }()
    
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
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.addSubview(imageView)
        stackView.addArrangedSubviews([titleLabel, descriptionLabel])
        contentView.addSubview(stackView)
        
        NSLayoutConstraint.activate([
            imageView.topAnchor.constraint(equalTo: contentView.topAnchor),
            imageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            imageView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            
            stackView.topAnchor.constraint(equalTo: imageView.bottomAnchor, constant: 30),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 24),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -24),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(_ model: OnboardingModel) {
        imageView.setImage(with: model.image)
        titleLabel.text = model.title
        descriptionLabel.text = model.description
        setNeedsLayout()
    }
}
