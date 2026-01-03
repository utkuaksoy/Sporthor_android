//
//  CKHeaderView.swift
//  ComponentKit
//
//  Created by derTurke on 4.03.2025.
//

import UIKit
import DesignKit

public final class CKHeaderView: UIView {
    // MARK: - UI Elements
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit
        imageView.isHidden = true
        imageView.widthAnchor.constraint(equalToConstant: 20).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 20).isActive = true
        return imageView
    }()
    
    private lazy var label: CKLabel = {
        let label = CKLabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([iconImageView, label])
        return stackView
    }()
    
    private let leadingCons: CGFloat
    private let trailingCons: CGFloat
    // MARK: - Members
    
    // MARK: - Initialize
    public init(delegate: CKLabelDelegate? = nil,
                text: String = "",
                textColor: UIColor = ColorName.contentSub800.color,
                backgroundColor: UIColor = .clear,
                numberOfLines: Int = 1,
                textAlignment: NSTextAlignment = .natural,
                lineBreakMode: NSLineBreakMode = .byTruncatingTail,
                font: UIFont? = .bold04Compact,
                isUserInteractionEnabled: Bool = false,
                image: UIImage? = nil,
                tag: Int = 0,
                leadingCons: CGFloat = 0,
                trailingCons: CGFloat = 0) {
        self.leadingCons = leadingCons
        self.trailingCons = trailingCons
        super.init(frame: .zero)
        label.ckDelegate = delegate
        label.text = text
        label.textColor = textColor
        label.backgroundColor = backgroundColor
        label.numberOfLines = numberOfLines
        label.textAlignment = textAlignment
        label.lineBreakMode = lineBreakMode
        label.font = font
        label.isUserInteractionEnabled = isUserInteractionEnabled
        label.tag = tag
        if let image = image {
            iconImageView.isHidden = false
            iconImageView.image = image
        }
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Custom Methods
    private func prepareUI() {
        addSubview(stackView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: leadingCons),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor, constant: trailingCons),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    public func updateText(_ text: String) {
        label.text = text
    }
    
    public func updateImage(_ image: UIImage?) {
        iconImageView.isHidden = image == nil
        iconImageView.image = image
    }
}
