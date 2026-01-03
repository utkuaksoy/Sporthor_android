//
//  AddPersonTrainingGroupViewController.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import UIKit
import ComponentKit

final class AddPersonTrainingGroupViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: AddPersonTrainingGroupPresenterProtocol {
        get { return self.basePresenter as! AddPersonTrainingGroupPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var clubImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.setCornerRadius(24)
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 48).isActive = true
        return imageView
    }()
    
    private lazy var clubLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .heading07)
        return label
    }()
    
    private lazy var groupNameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var clubAndGroupStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([clubLabel, groupNameLabel])
        return stackView
    }()
    
    private lazy var seperatorView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var searchBar: CKSearchBar = {
        let searchBar = CKSearchBar(delegate: self,
                                    textColor: DesignKitColorName.contentStrong900.color,
                                    placeholder: "Kişi Ara",
                                    placeholderColor: DesignKitColorName.contentSoft600.color,
                                    backgroundColor: DesignKitColorName.contentWeak100.color,
                                    cornerRadius: 22,
                                    borderWidth: 1,
                                    selectedBorderColor: DesignKitColorName.borderSub300.color,
                                    font: .body04Compact,
                                    image: Asset.searchbarSearch.image,
                                    clearImage: Asset.searchbarClose.image,
                                    cancelButtonTitle: "İptal",
                                    cancelButtonTitleColor: DesignKitColorName.contentStrong900.color,
                                    cancelButtonFont: .body04Compact)
        searchBar.translatesAutoresizingMaskIntoConstraints = false
        return searchBar
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.separatorStyle = .none
        tableView.backgroundColor = .clear
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.showsVerticalScrollIndicator = false
        tableView.contentInset = .init(top: 16, left: .zero, bottom: 16, right: .zero)
        tableView.removeEmptyCell()
        return tableView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Grup Üyelerini Davet Et",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 0)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.navigationItem.setHidesBackButton(true, animated: false)
        if let navCon = navigationController as? CustomNavigationController {
            navCon.customDelegate = self
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.navigationItem.setHidesBackButton(false, animated: false)
    }
    
    // MARK: - Custom Methods
}

// MARK: - AddPersonTrainingGroupPresenterDelegate
extension AddPersonTrainingGroupViewController: AddPersonTrainingGroupPresenterDelegate {
    func prepareUI() {
        view.addSubview(clubImageView)
        view.addSubview(clubAndGroupStackView)
        view.addSubview(seperatorView)
        view.addSubview(searchBar)
        view.addSubview(tableView)
        view.addSubview(continueButton)
        
        NSLayoutConstraint.activate([
            clubImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 12),
            clubImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            
            clubAndGroupStackView.centerYAnchor.constraint(equalTo: clubImageView.centerYAnchor),
            clubAndGroupStackView.leadingAnchor.constraint(equalTo: clubImageView.trailingAnchor, constant: 16),
            clubAndGroupStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            
            seperatorView.topAnchor.constraint(equalTo: clubImageView.bottomAnchor, constant: 12),
            seperatorView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            seperatorView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            seperatorView.heightAnchor.constraint(equalToConstant: 1),
            
            searchBar.topAnchor.constraint(equalTo: seperatorView.bottomAnchor, constant: 16),
            searchBar.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            searchBar.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            searchBar.heightAnchor.constraint(equalToConstant: 44),
            
            tableView.topAnchor.constraint(equalTo: searchBar.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32),
            continueButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            continueButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            continueButton.topAnchor.constraint(equalTo: tableView.bottomAnchor)
        ])
        
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isBackChevronLeft = true
        }
    }
    
    func prepareClub(image: String, name: String, groupName: String) {
        clubImageView.setImage(with: image)
        clubLabel.text = name
        groupNameLabel.text = groupName
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

// MARK: - CKSearchBarDelegate
extension AddPersonTrainingGroupViewController: CKSearchBarDelegate {
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {
        presenter.search(text)
    }
    
    func searchBarDidCancel(_ searchBar: CKSearchBar) {
        presenter.searchCancel()
    }
}

// MARK: - UITableViewDataSource
extension AddPersonTrainingGroupViewController: UITableViewDataSource {    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch presenter.viewType {
        case .search:
            return presenter.searches.isEmpty ? 1 : presenter.searches.count
        case .selection:
            return presenter.selectedSearches.isEmpty ? 1 : presenter.selectedSearches.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let isSearchMode = presenter.viewType == .search
        let list = isSearchMode ? presenter.searches : presenter.selectedSearches

        guard !list.isEmpty else {
            let cell = EmptyTableViewCell.dequeue(from: tableView, at: indexPath)
            let description = isSearchMode
                ? "Arama sonuçları bulunamadı. Lütfen farklı bir arama yapınız."
                : "Henüz seçili grup üyesi yok. Grup üyelerini eklemek için arama yapabilirsiniz."
            cell.bind(image: Asset.infoAlert.image, description: description)
            return cell
        }

        let model = list[indexPath.row]

        let cell = ChatUserCell.dequeue(from: tableView, at: indexPath)
        cell.bind(name: model.userName,
                  role: model.name,
                  imageUrl: model.image,
                  isGroupSelection: true,
                  isSelected: model.isSelected)
        return cell
    }
}

extension AddPersonTrainingGroupViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRowAt(indexPath)
    }
}

// MARK: - CKButtonDelegate
extension AddPersonTrainingGroupViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedButton(tag)
    }
}

extension AddPersonTrainingGroupViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}
