//
//  CKPostHeaderView.swift
//  ComponentKit
//
//  Created by derTurke on 29.03.2025.
//

import UIKit
import DesignKit

public final class CKPostHeaderView: UIView {
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.clipsToBounds = true
        return imageView
    }()
    
    private lazy var tripleDotButton: CKButton = {
        let button = CKButton(delegate: self)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var label: CKLabel = {
        let label = CKLabel(delegate: self,
                            font: .bold04Compact,
                            isUserInteractionEnabled: true)
        return label
    }()
    
    // MARK: - Members
    private weak var delegate: CKPostHeaderViewDelegate?
    private var username: String = ""
    private var userId: String = ""
    
    // MARK: - Initialize
    public override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        stackView.addArrangedSubviews([profileImageView, label])
        addSubview(stackView)
        addSubview(tripleDotButton)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            profileImageView.heightAnchor.constraint(equalTo: stackView.heightAnchor),
            profileImageView.widthAnchor.constraint(equalTo: stackView.heightAnchor),
            tripleDotButton.leadingAnchor.constraint(equalTo: stackView.trailingAnchor, constant: 8),
            tripleDotButton.trailingAnchor.constraint(equalTo: trailingAnchor),
            tripleDotButton.heightAnchor.constraint(equalTo: stackView.heightAnchor),
            tripleDotButton.widthAnchor.constraint(equalTo: stackView.heightAnchor)
        ])
    }
    
    public override func layoutSubviews() {
        super.layoutSubviews()
        
    }
    
    // MARK: - Custom Methods
    public func bind(with image: String,
                     placeholderImage: UIImage? = nil,
                     username: String,
                     usernameTextColor: UIColor? = ColorName.contentStrong900.color,
                     userId: String,
                     imageCornerRadius: CGFloat = 0,
                     tripleDotButtonImage: UIImage? = nil,
                     delegate: CKPostHeaderViewDelegate? = nil) {
        self.delegate = delegate
        self.username = username
        self.userId = userId
        profileImageView.setImage(with: image, placeholder: placeholderImage, showIndicator: false)
        profileImageView.setCornerRadius(imageCornerRadius)
        label.text = username
        label.textColor = usernameTextColor
        tripleDotButton.setImage(tripleDotButtonImage)
    }
}

// MARK: - CKLabelDelegate
extension CKPostHeaderView: CKLabelDelegate {
    public func didTapCKLabel(tag: Int) {
        guard let delegate,
              !username.isEmpty,
              !userId.isEmpty else { return }
        delegate.didTappedUsernameInHeaderView(username, userId: userId)
    }
}

// MARK: - CKButtonDelegate
extension CKPostHeaderView: CKButtonDelegate {
    public func ckButtonDidTap(tag: Int) {
        guard let delegate else { return }
        delegate.didTappedTripleButton()
    }
}
