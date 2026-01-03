//
//  HorizontalUsernameTableViewCell.swift
//  Sporthor
//
//  Created by derTurke.
//

import UIKit

enum HorizontalUsernameTableViewCellType {
    case normal
    case notification
}

protocol HorizontalUsernameTableViewCellDelegate: AnyObject {
    func didSelectItem(_ text: String)
    func didSelectItem(model: Any)
}

extension HorizontalUsernameTableViewCellDelegate {
    func didSelectItem(_ text: String) {}
    func didSelectItem(model: Any) {}
}

final class HorizontalUsernameTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let collectionView = UICollectionView(frame: .zero,
                                              collectionViewLayout: layout)
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.backgroundColor = .clear
        return collectionView
    }()
    
    private var collectionViewLeadingCons: NSLayoutConstraint!
    private var collectionViewTrailingCons: NSLayoutConstraint!
    
    // MARK: - Members
    private weak var delegate: HorizontalUsernameTableViewCellDelegate?
    private var type: HorizontalUsernameTableViewCellType = .normal
    private var data: [String] = []
    private var headerTabs: [Any] = []
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    // MARK: - Custom Methods
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.addSubview(collectionView)
        
        collectionViewLeadingCons = collectionView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor)
        collectionViewTrailingCons = collectionView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor)
        
        NSLayoutConstraint.activate([
            collectionView.topAnchor.constraint(equalTo: contentView.topAnchor),
            collectionViewLeadingCons,
            collectionViewTrailingCons,
            collectionView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    func bind(delegate: HorizontalUsernameTableViewCellDelegate? = nil,
              data: [String],
              leadingCons: CGFloat = 0,
              trailingCons: CGFloat = 0) {
        self.delegate = delegate
        self.data = data
        setupCons(leadingCons: leadingCons, trailingCons: trailingCons)
    }
    
    func bind(delegate: HorizontalUsernameTableViewCellDelegate? = nil,
              model: [Any],
              type: HorizontalUsernameTableViewCellType,
              leadingCons: CGFloat = 0,
              trailingCons: CGFloat = 0) {
        self.delegate = delegate
        self.headerTabs = model
        self.type = type
        setupCons(leadingCons: leadingCons, trailingCons: trailingCons)
    }
    
    private func setupCons(leadingCons: CGFloat, trailingCons: CGFloat) {
        NSLayoutConstraint.deactivate([
            collectionViewLeadingCons,
            collectionViewTrailingCons
        ])
        
        collectionViewLeadingCons = collectionView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor,
                                                                            constant: leadingCons)
        collectionViewTrailingCons = collectionView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor,
                                                                              constant: trailingCons)
        
        collectionViewLeadingCons.isActive = true
        collectionViewTrailingCons.isActive = true
    }
}

extension HorizontalUsernameTableViewCell: UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        switch type {
        case .normal:
            return data.count
        case .notification:
            return headerTabs.count
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = UsernameCollectionViewCell.dequeue(from: collectionView, at: indexPath)
        switch type {
        case .normal:
            cell.bind(text: data[indexPath.item])
        case .notification:
            let notification = (headerTabs[indexPath.item] as! NotificationHeaderModel)
            cell.bind(text: notification.title, isSelected: notification.isSelected)
        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard let delegate else { return }
        switch type {
        case .normal:
            delegate.didSelectItem(data[indexPath.item])
        case .notification:
            delegate.didSelectItem(model: headerTabs[indexPath.item])
        }
    }
}

extension HorizontalUsernameTableViewCell: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        var text: String = ""
        switch type {
        case .normal:
            text = data[indexPath.item]
        case .notification:
            text = (headerTabs[indexPath.item] as! NotificationHeaderModel).title
        }
        let textWidth = text.width(withConstrainedHeight: collectionView.bounds.height,
                                   font: .body04Compact)
        let padding: CGFloat = 24
        
        
        return CGSize(width: textWidth + padding, height: collectionView.bounds.height)
    }
}
