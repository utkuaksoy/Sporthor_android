//
//  CKClubInfoView.swift
//  ComponentKit
//
//  Created by GÜRHAN YUVARLAK on 29.10.2025.
//

import UIKit
import DesignKit

public final class CKClubInfoView: UIView {
    // MARK: - UI Elements
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.widthAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.layer.cornerRadius = 24
        imageView.clipsToBounds = true
        return imageView
    }()
    
    private lazy var nameLabel: CKLabel = {
        let label = CKLabel(textColor: ColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .heading07)
        return label
    }()
    
    private lazy var infoLabel: CKLabel = {
        let label = CKLabel(textColor: ColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var infoStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.addArrangedSubviews([nameLabel, infoLabel])
        return stackView
    }()
    
    private lazy var horizontalStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 16)
        stackView.addArrangedSubviews([imageView, infoStackView])
        return stackView
    }()
    
    private lazy var separatorView: UIView = {
        let view = UIView()
        view.heightAnchor.constraint(equalToConstant: 1).isActive = true
        view.backgroundColor = ColorName.borderSoft200.color
        return view
    }()
    
    private lazy var contentStackView: CKStackView = {
        let stackView = CKStackView(axis: .vertical, spacing: 16)
        stackView.addArrangedSubviews([horizontalStackView, separatorView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    public init(image: String = "",
                name: String = "",
                info: String? = nil,
                isHiddenSeparator: Bool = false) {
        super.init(frame: .zero)
        setupView()
        bind(image: image,
             name: name,
             info: info,
             isHiddenSeparator: isHiddenSeparator)
    }
    
    required init?(coder: NSCoder) {
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
    public func bind(image: String = "",
                     name: String = "",
                     info: String? = nil,
                     isHiddenSeparator: Bool = false) {
        imageView.setImage(with: image)
        nameLabel.text = name
        infoLabel.text = info
        infoLabel.isHidden = info?.isEmpty ?? true
        separatorView.isHidden = isHiddenSeparator
    }
}
