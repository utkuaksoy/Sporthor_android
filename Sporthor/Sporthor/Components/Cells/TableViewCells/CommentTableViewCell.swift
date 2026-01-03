//
//  CommentTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 30.04.2025.
//

import UIKit
import ComponentKit

protocol CommentTableViewCellDelegate: AnyObject {
    
}

final class CommentTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.setCornerRadius(20)
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var usernameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color, font: .bold04Compact)
        return label
    }()
    
    private lazy var commentLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color, numberOfLines: 0, font: .body04Compact)
        return label
    }()
    
    private lazy var commentStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.addArrangedSubviews([usernameLabel, commentLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var likeButton: CKButton = {
        let button = CKButton(delegate: self)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    private weak var delegate: CommentTableViewCellDelegate?
    
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
        contentView.addSubview(profileImageView)
        contentView.addSubview(commentStackView)
        contentView.addSubview(likeButton)
        
        NSLayoutConstraint.activate([
            profileImageView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            profileImageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            profileImageView.heightAnchor.constraint(equalToConstant: 40),
            profileImageView.widthAnchor.constraint(equalToConstant: 40),
            
            commentStackView.leadingAnchor.constraint(equalTo: profileImageView.trailingAnchor, constant: 8),
            commentStackView.topAnchor.constraint(equalTo: profileImageView.topAnchor),
            commentStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            
            likeButton.topAnchor.constraint(equalTo: profileImageView.topAnchor),
            likeButton.leadingAnchor.constraint(equalTo: commentStackView.trailingAnchor, constant: 8),
            likeButton.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            likeButton.widthAnchor.constraint(equalToConstant: 16),
            likeButton.heightAnchor.constraint(equalToConstant: 16)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: CommentTableViewCellDelegate? = nil, model: CommentModel) {
        self.delegate = delegate
        profileImageView.setImage(with: model.profileImageUrl, placeholder: Asset.errorUserImage.image)
        usernameLabel.text = model.username
        commentLabel.text = model.text
        likeButton.isHidden = true
    }
}

extension CommentTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        
    }
}
