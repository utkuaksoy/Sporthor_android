//
//  CurrentTeamsContainerCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class CurrentTeamsContainerCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements
    
    private let containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let titleLabelContainerView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentStrong900.color,
            font: .bold03Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var containerStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .fill,
            spacing: 16
        )
        stackView.addArrangedSubviews([titleLabelContainerView, collectionView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumLineSpacing = 8
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.backgroundColor = .white
        collectionView.register(
            TeamCollectionViewCell.self,
            forCellWithReuseIdentifier: TeamCollectionViewCell.reuseIdentifier
        )
        collectionView.dataSource = self
        collectionView.delegate = self
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.contentInset = UIEdgeInsets(top: .zero, left: 16, bottom: .zero, right: 16)
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    // MARK: - Private Properties
    
    private var data: TeamsDataModel?
    
    // MARK: - Initializers
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configure
    
    func configure(
        with data: TeamsDataModel,
        at indexPath: IndexPath
    ) {
        self.data = data
        configureTitleLabel(with: data.title)
        collectionView.reloadData()
    }
    
    private func configureTitleLabel(with name: String?) {
        titleLabel.text = name
    }
}

// MARK: UICollectionView Delegate&Datasource

extension CurrentTeamsContainerCell: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    func collectionView(
        _ collectionView: UICollectionView,
        numberOfItemsInSection section: Int
    ) -> Int {
        return data?.teams?.count ?? .zero
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: TeamCollectionViewCell.reuseIdentifier, for: indexPath) as? TeamCollectionViewCell,
              let item = data?.teams?[safe: indexPath.row] else { return UICollectionViewCell() }
        cell.configure(with: item.teamLogoImageUrl, title: item.teamName)
        return cell
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        sizeForItemAt indexPath: IndexPath
    ) -> CGSize {
        return CGSize(width: (collectionView.frame.size.width - 32) / 2, height: 40)
    }
}

// MARK: - Setup

private extension CurrentTeamsContainerCell {
    func setupViews() {
        contentView.addSubview(containerView)
        titleLabelContainerView.addSubview(titleLabel)
        containerView.addSubview(containerStackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 24),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            containerStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            containerStackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            
            titleLabelContainerView.heightAnchor.constraint(equalToConstant: 20),
            titleLabel.leadingAnchor.constraint(equalTo: titleLabelContainerView.leadingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: titleLabelContainerView.trailingAnchor, constant: -16),
            titleLabel.topAnchor.constraint(equalTo: titleLabelContainerView.topAnchor),
            titleLabel.bottomAnchor.constraint(equalTo: titleLabelContainerView.bottomAnchor),
        ])
    }
}
