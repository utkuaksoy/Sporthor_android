//
//  CKImageTitleInfoRightImageView.swift
//  ComponentKit
//
//  Created by GÜRHAN YUVARLAK on 31.10.2025.
//

import UIKit
import DesignKit

public final class CKImageTitleInfoRightImageView: UIView {
    // MARK: - UI Elements
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.widthAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.setCornerRadius(24)
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: ColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        return label
    }()
    
    private lazy var infoLabel: CKLabel = {
        let label = CKLabel(textColor: ColorName.contentSoft600.color,
                            numberOfLines: 0,
                            font: .body06Compact)
        return label
    }()
    
    private lazy var titleInfoStackView: CKStackView = {
        let stackView = CKStackView(spacing: 2)
        stackView.addArrangedSubviews([titleLabel, infoLabel])
        return stackView
    }()
    
    private lazy var rightImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var contentStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    alignment: .center,
                                    spacing: 8)
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 8, left: 16, bottom: 8, right: 16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([imageView, titleInfoStackView, rightImageView])
        return stackView
    }()
    
    // MARK: - Members
    
    // MARK: - Initializers
    public init(image: String? = nil,
                placeholderImage: UIImage? = nil,
                title: String? = nil,
                info: String? = nil,
                rightImage: UIImage? = nil) {
        super.init(frame: .zero)
        setupView()
        bind(image: image,
             placeholderImage: placeholderImage,
             title: title,
             info: info,
             rightImage: rightImage)
    }
    
    public required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        addSubview(contentStackView)
        
        NSLayoutConstraint.activate([
            contentStackView.topAnchor.constraint(equalTo: topAnchor),
            contentStackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            contentStackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            contentStackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    public func bind(image: String? = nil,
                     placeholderImage: UIImage? = nil,
                     title: String? = nil,
                     info: String? = nil,
                     rightImage: UIImage? = nil) {
        imageView.setImage(with: image, placeholder: placeholderImage, errorImage: placeholderImage)
        
        titleLabel.text = title
        titleLabel.isHidden = title?.isEmpty ?? true
        
        infoLabel.text = info
        infoLabel.isHidden = info?.isEmpty ?? true
        
        rightImageView.image = rightImage
        rightImageView.isHidden = rightImage == nil
    }
}
