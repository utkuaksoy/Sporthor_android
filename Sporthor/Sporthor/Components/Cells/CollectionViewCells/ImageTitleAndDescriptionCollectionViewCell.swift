//
//  ImageTitleAndDescriptionCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//

import UIKit
import ComponentKit

protocol ImageTitleAndDescriptionCollectionViewCellDelegate: AnyObject {
    func didTappedDescriptionLabel()
}

extension ImageTitleAndDescriptionCollectionViewCellDelegate {
    func didTappedDescriptionLabel() {}
}

final class ImageTitleAndDescriptionCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var imageBorderView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        view.setCornerRadius(24)
        view.clipsToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.setCornerRadius(20)
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(delegate: self,
                            textColor: DesignKitColorName.contentSoft600.color,
                            numberOfLines: 0,
                            textAlignment: .right,
                            font: .body04Compact,
                            isUserInteractionEnabled: true,
                            tag: 1)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var separatorView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        view.heightAnchor.constraint(equalToConstant: 1).isActive = true
        return view
    }()
    
    // MARK: - Members
    private weak var delegate: ImageTitleAndDescriptionCollectionViewCellDelegate?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        imageBorderView.addSubview(imageView)
        contentView.addSubview(imageBorderView)
        contentView.addSubview(titleLabel)
        contentView.addSubview(descriptionLabel)
        contentView.addSubview(separatorView)

        titleLabel.setContentHuggingPriority(.defaultHigh, for: .horizontal)
        descriptionLabel.setContentHuggingPriority(.defaultLow, for: .horizontal)

        titleLabel.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
        descriptionLabel.setContentCompressionResistancePriority(.defaultHigh, for: .horizontal)

        NSLayoutConstraint.activate([
            imageBorderView.topAnchor.constraint(equalTo: contentView.topAnchor),
            imageBorderView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            imageBorderView.widthAnchor.constraint(equalToConstant: 48),
            imageBorderView.heightAnchor.constraint(equalToConstant: 48),

            imageView.centerXAnchor.constraint(equalTo: imageBorderView.centerXAnchor),
            imageView.centerYAnchor.constraint(equalTo: imageBorderView.centerYAnchor),
            imageView.widthAnchor.constraint(equalToConstant: 40),
            imageView.heightAnchor.constraint(equalToConstant: 40),

            titleLabel.topAnchor.constraint(equalTo: imageBorderView.topAnchor),
            titleLabel.leadingAnchor.constraint(equalTo: imageBorderView.trailingAnchor, constant: 8),
            titleLabel.bottomAnchor.constraint(equalTo: imageBorderView.bottomAnchor),
            
            descriptionLabel.leadingAnchor.constraint(equalTo: titleLabel.trailingAnchor, constant: 8),
            descriptionLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            descriptionLabel.centerYAnchor.constraint(equalTo: imageBorderView.centerYAnchor),

            separatorView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            separatorView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            separatorView.heightAnchor.constraint(equalToConstant: 1),
            separatorView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }

    
    // MARK: - Custom Methods
    func bind(delegate: ImageTitleAndDescriptionCollectionViewCellDelegate? = nil,
              image: String,
              imageBorderWidth: CGFloat = 1,
              imageBorderColor: UIColor = DesignKitColorName.borderSoft200.color,
              title: String,
              description: String = "",
              isHiddenSeparatorView: Bool = false) {
        self.delegate = delegate
        imageView.kf.setImage(with: URL(string: image))
        imageBorderView.setBorderWidth(imageBorderWidth)
        imageBorderView.setBorderColor(imageBorderColor)
        titleLabel.text = title
        descriptionLabel.text = description
        descriptionLabel.isHidden = description.isEmpty
        separatorView.isHidden = isHiddenSeparatorView
    }
}

extension ImageTitleAndDescriptionCollectionViewCell: CKLabelDelegate {
    func didTapCKLabel(tag: Int) {
        switch tag {
        case 1:
            delegate?.didTappedDescriptionLabel()
        default:
            break
        }
    }
}
