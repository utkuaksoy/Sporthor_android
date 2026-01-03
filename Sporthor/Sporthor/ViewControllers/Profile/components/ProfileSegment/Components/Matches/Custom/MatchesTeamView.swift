//
//  ProfileMatchesTeamView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.03.2025.
//

import DesignKit
import ComponentKit
import Kingfisher
import UIKit

final class MatchesTeamView: UIView {
    
    // MARK: - Private UI Elements

    private lazy var containerStackView: CKStackView = {
        let stack = CKStackView(axis: .horizontal)
        stack.spacing = 12
        stack.alignment = .center
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    private lazy var teamLogoImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = Asset.exIcon.image
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        imageView.layer.cornerRadius = 24
        imageView.setBorderWidth(1)
        imageView.setBorderColor(ColorName.borderSoft200.color)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var teamNameLabel: CKLabel = {
        let label = CKLabel(
            text: "Eczacıbaşı Spor Kulübü U17",
            textColor: ColorName.contentStrong900.color,
            numberOfLines: 2,
            font: .interTight500
        )
        label.font = .systemFont(ofSize: 14, weight: .medium)
        label.textColor = ColorName.contentStrong900.color
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var scoreLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentStrong900.color,
            backgroundColor: ColorName.backgroundSoft200.color,
            textAlignment: .center,
            font: .heading06
        )
        label.setCornerRadius(4)
        label.clipsToBounds = true
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Initializar

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configure Methods

    func configure(with model: MatchTeamModel) {
        teamNameLabel.text = model.teamName
        scoreLabel.text = "\(model.score)"
        teamLogoImageView.setImage(with: model.teamLogoImageUrl)
    }
}

// MARK: - Setup

private extension MatchesTeamView {
    func setupViews() {
        addSubview(containerStackView)
        containerStackView.addArrangedSubview(teamLogoImageView)
        containerStackView.addArrangedSubview(teamNameLabel)
        containerStackView.addArrangedSubview(scoreLabel)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerStackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            containerStackView.topAnchor.constraint(equalTo: topAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            teamLogoImageView.widthAnchor.constraint(equalToConstant: 48),
            teamLogoImageView.heightAnchor.constraint(equalToConstant: 48),
            
            scoreLabel.widthAnchor.constraint(equalToConstant: 24)
        ])
    }
}
