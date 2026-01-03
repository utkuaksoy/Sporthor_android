//
//  CKPostLikeView.swift
//  ComponentKit
//
//  Created by derTurke on 29.03.2025.
//

import UIKit
import DesignKit

public final class CKPostLikeView: UIView {
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .leading, spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    private lazy var profileImagesStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: -8)
        return stackView
    }()
    
    private lazy var label: CKLabel = {
        let label = CKLabel(textColor: DesignKit.ColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact,
                            isUserInteractionEnabled: true)
        return label
    }()
    
    // MARK: - Members
    private weak var delegate: CKPostLikeViewDelegate?
    private var username: String = ""
    private var userId: String = ""
    
    // MARK: - Initializers
    public init(delegate: CKPostLikeViewDelegate? = nil,
                likes: [String] = [],
                username: String = "",
                userId: String = "") {
        super.init(frame: .zero)
        bind(likes: likes, username: username, userId: "")
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Custom Methods
    public func bind(delegate: CKPostLikeViewDelegate? = nil,
                     likes: [String],
                     placeholderImage: UIImage? = nil,
                     username: String,
                     userId: String) {
        self.delegate = delegate
        self.username = username
        self.userId = userId
        stackView.removeAllArrangedSubviews()
        profileImagesStackView.removeAllArrangedSubviews()
        for imageName in likes {
            let imageView = UIImageView()
            imageView.setImage(with: imageName, placeholder: placeholderImage)
            imageView.widthAnchor.constraint(equalToConstant: 18).isActive = true
            imageView.heightAnchor.constraint(equalToConstant: 18).isActive = true
            imageView.clipsToBounds = true
            imageView.contentMode = .scaleAspectFill
            profileImagesStackView.addArrangedSubview(imageView)
            imageView.setCornerRadius(9)
        }
        
        if username.isEmpty {
            label.text = "Henüz kimse beğenmedi. İlk beğenen sen ol"
        } else {
            let attributedText = NSMutableAttributedString(
                string: username,
                attributes: [.font: UIFont.bold04Compact]
            )
            
            attributedText.append(NSAttributedString(
                string: " ve diğer kişiler beğendi",
                attributes: [.font: UIFont.body04Compact]
            ))
            
            label.attributedText = attributedText
            let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTapLabel(_:)))
            label.addGestureRecognizer(tapGesture)
        }
        
        if likes.count > 0 {
            stackView.addArrangedSubview(profileImagesStackView)
        }
        stackView.addArrangedSubview(label)
        
        addSubview(stackView)
        NSLayoutConstraint.activate([
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    @objc private func handleTapLabel(_ gesture: UITapGestureRecognizer) {
        label.detectTap(on: username, gesture: gesture) { [weak self] in
            guard let self,
                  let delegate = self.delegate,
                  !userId.isEmpty else { return }
            delegate.didTappedUsernameInLikeView(self.username, userId: self.userId)
        }
    }
    
    public func update(likes: [String],
                       placeholderImage: UIImage?,
                       username: String,
                       userId: String) {
        bind(delegate: delegate,
             likes: likes,
             placeholderImage: placeholderImage,
             username: username,
             userId: userId)
    }
}
