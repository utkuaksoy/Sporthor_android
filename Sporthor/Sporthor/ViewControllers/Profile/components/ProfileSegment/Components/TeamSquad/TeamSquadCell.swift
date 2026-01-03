//
//  TamSquadCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 16.03.2025.
//

import ComponentBaseKit
import ComponentKit
import DesignKit
import UIKit

final class TeamSquadCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements

    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var containerStackView: CKStackView = {
        let stackview = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .fill,
            spacing: 16
        )
        stackview.addArrangedSubviews([titleLabel, collectionView])
        stackview.translatesAutoresizingMaskIntoConstraints = false
        return stackview
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            text: "Teknik Kadro",
            textColor: ColorName.contentStrong900.color,
            textAlignment: .left,
            font: .bold03Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        let collectionView = UICollectionView(
            frame: .zero,
            collectionViewLayout: layout
        )
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.backgroundColor = .clear
        collectionView.register(
            TeamMemberCell.self,
            forCellWithReuseIdentifier: TeamMemberCell.reuseIdentifier
        )
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    // MARK: - Private Properties
    
    private var teamMembers: [TeamMemberItem]?
    
    // MARK: - Initializer

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func configure(with model: TeamGroup?) {
        self.teamMembers = model?.members
        configureTitleLabel(with: model?.title)
        collectionView.reloadData()
    }
    
    private func configureTitleLabel(with title: String?) {
        titleLabel.text = title
    }
}

extension TeamSquadCell: UICollectionViewDataSource {
    func collectionView(
        _ collectionView: UICollectionView,
        numberOfItemsInSection section: Int
    ) -> Int {
        return teamMembers?.count ?? .zero
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: TeamMemberCell.reuseIdentifier,
            for: indexPath
        ) as? TeamMemberCell else { return UICollectionViewCell() }
        cell.configure(with: teamMembers?[safe: indexPath.item])
        return cell
    }
}

extension TeamSquadCell: UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        sizeForItemAt indexPath: IndexPath
    ) -> CGSize {
        return CGSize(width: collectionView.frame.size.width, height: 48)
    }
}

// MARK: - Setup

private extension TeamSquadCell {
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(containerStackView)
    }

    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            containerStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            containerStackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            
            titleLabel.heightAnchor.constraint(equalToConstant: 20)
        ])
    }
}
