//
//  RectangleImageTitleCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//

import UIKit
import ComponentKit

final class RectangleImageTitleCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var backgroundImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var mainImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.layer.cornerRadius = 36
        imageView.layer.masksToBounds = true
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            textAlignment: .center,
                            font: .bold04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Members
    
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
        contentView.addSubview(backgroundImageView)
        backgroundImageView.addSubview(mainImageView)
        backgroundImageView.addSubview(titleLabel)
        
        NSLayoutConstraint.activate([
            backgroundImageView.topAnchor.constraint(equalTo: contentView.topAnchor),
            backgroundImageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            backgroundImageView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            backgroundImageView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            mainImageView.topAnchor.constraint(equalTo: backgroundImageView.topAnchor, constant: 20),
            mainImageView.centerXAnchor.constraint(equalTo: backgroundImageView.centerXAnchor),
            mainImageView.heightAnchor.constraint(equalToConstant: 72),
            mainImageView.widthAnchor.constraint(equalToConstant: 72),
            
            titleLabel.topAnchor.constraint(equalTo: mainImageView.bottomAnchor, constant: 16),
            titleLabel.leadingAnchor.constraint(equalTo: backgroundImageView.leadingAnchor, constant: 8),
            titleLabel.trailingAnchor.constraint(equalTo: backgroundImageView.trailingAnchor, constant: -8),
            titleLabel.bottomAnchor.constraint(equalTo: backgroundImageView.bottomAnchor, constant: -14)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(image: String,
              title: String,
              isSelected: Bool = false) {
        backgroundImageView.image = isSelected ? Asset.gradientGreenOctagon.image : Asset.greyRectangle.image
        mainImageView.setImage(with: image)
        titleLabel.text = title
    }
    
    func bind(model: SelectionModel) {
        backgroundImageView.image = model.isSelected ? Asset.gradientGreenOctagon.image : Asset.greyRectangle.image
        mainImageView.setImage(with: model.image)
        titleLabel.text = model.value
    }
    
}
