//
//  CKImageInfoView.swift
//  ComponentKit
//
//  Created by GÜRHAN YUVARLAK on 31.10.2025.
//

import UIKit
import DesignKit

public protocol CKImageTitleViewDelegate: AnyObject {
    func didTappedCKImageTitleView(_ tag: Int)
}

public extension CKImageTitleViewDelegate {
    func didTappedCKImageTitleView(_ tag: Int) {}
}

public final class CKImageTitleView: UIView {
    // MARK: - UI Elements
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.widthAnchor.constraint(equalToConstant: 40).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 40).isActive = true
        imageView.setCornerRadius(20)
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(numberOfLines: 0)
        return label
    }()
    
    private lazy var rightImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    alignment: .center,
                                    spacing: 8)
        stackView.addArrangedSubviews([imageView, titleLabel, rightImageView])
        stackView.isUserInteractionEnabled = true
        stackView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTappedView)))
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 16, left: 16, bottom: 16, right: 16)
        stackView.setCornerRadius(8)
        stackView.clipsToBounds = true
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var backgroundImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.setCornerRadius(8)
        return imageView
    }()
    
    // MARK: - Members
    private weak var delegate: CKImageTitleViewDelegate?
    
    // MARK: - Initializers
    public init(delegate: CKImageTitleViewDelegate? = nil,
                image: String? = nil,
                title: String? = nil,
                titleColor: UIColor = .clear,
                titleFont: UIFont = .systemFont(ofSize: 14),
                rightImage: UIImage? = nil,
                backgroundColor: UIColor = .clear,
                backgroundImage: UIImage? = nil,
                tag: Int = 0) {
        super.init(frame: .zero)
        setupView()
        bind(delegate: delegate,
             image: image,
             title: title,
             titleColor: titleColor,
             titleFont: titleFont,
             rightImage: rightImage,
             backgroundColor: backgroundColor,
             backgroundImage: backgroundImage,
             tag: tag)
    }
    
    public required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    private func setupView() {
        addSubview(backgroundImageView)
        addSubview(stackView)
        NSLayoutConstraint.activate([
            backgroundImageView.topAnchor.constraint(equalTo: topAnchor),
            backgroundImageView.leadingAnchor.constraint(equalTo: leadingAnchor),
            backgroundImageView.trailingAnchor.constraint(equalTo: trailingAnchor),
            backgroundImageView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    public func bind(delegate: CKImageTitleViewDelegate? = nil,
                     image: String? = nil,
                     title: String? = nil,
                     titleColor: UIColor = .clear,
                     titleFont: UIFont = .systemFont(ofSize: 14),
                     rightImage: UIImage? = nil,
                     backgroundColor: UIColor = .clear,
                     backgroundImage: UIImage? = nil,
                     tag: Int = 0) {
        self.delegate = delegate
        imageView.setImage(with: image)
        titleLabel.text = title
        titleLabel.textColor = titleColor
        titleLabel.font = titleFont
        rightImageView.image = rightImage
        stackView.backgroundColor = backgroundColor
        backgroundImageView.image = backgroundImage
        backgroundImageView.isHidden = backgroundImage == nil
        self.tag = tag
    }
    
    @objc private func didTappedView() {
        delegate?.didTappedCKImageTitleView(self.tag)
    }
}
