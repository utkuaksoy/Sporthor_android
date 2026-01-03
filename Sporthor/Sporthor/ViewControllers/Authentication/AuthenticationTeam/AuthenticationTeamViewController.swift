//
//  AuthenticationTeamViewController.swift
//  Sporthor
//
//  Created by derTurke on 24.02.2025.
//
//

import UIKit
import ComponentKit

final class AuthenticationTeamViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: AuthenticationTeamPresenterProtocol {
        get { return self.basePresenter as! AuthenticationTeamPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var textStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color, numberOfLines: 0, font: .heading04)
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color, numberOfLines: 0, font: .body04Compact)
        return label
    }()
    
    private lazy var searchBar: CKSearchBar = {
        let searchBar = CKSearchBar(delegate: self,
                                    textColor: DesignKitColorName.contentStrong900.color,
                                    placeholder: "Kulübünü Ara",
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
        tableView.allowsSelection = false
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.removeEmptyCell()
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 12, right: 0)
        tableView.backgroundColor = .clear
        return tableView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: DesignKitL10n.Authentication.Team.buttonTitle,
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 0)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    private lazy var secondButton: CKButton = {
        let button = CKButton(delegate: self,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 1)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    private lazy var buttonsStackView: CKStackView = {
        let stackView = CKStackView(spacing: 12)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([continueButton, secondButton])
        return stackView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.viewWillAppear()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.viewWillDisappear()
    }
    
    // MARK: - Custom Methods
}

// MARK: - AuthenticationTeamPresenterDelegate
extension AuthenticationTeamViewController: AuthenticationTeamPresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String) {
        titleLabel.text = title
        descriptionLabel.text = description
    }
    
    func prepareUI() {
        textStackView.addArrangedSubviews([titleLabel, descriptionLabel])
        view.addSubview(textStackView)
        view.addSubview(searchBar)
        view.addSubview(tableView)
        view.addSubview(buttonsStackView)
        
        NSLayoutConstraint.activate([
            textStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 24),
            textStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            textStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            searchBar.topAnchor.constraint(equalTo: textStackView.bottomAnchor, constant: 24),
            searchBar.leadingAnchor.constraint(equalTo: textStackView.leadingAnchor),
            searchBar.trailingAnchor.constraint(equalTo: textStackView.trailingAnchor),
            searchBar.heightAnchor.constraint(equalToConstant: 48),
            
            tableView.topAnchor.constraint(equalTo: searchBar.bottomAnchor, constant: 24),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: continueButton.topAnchor, constant: -12),
            
            buttonsStackView.leadingAnchor.constraint(equalTo: textStackView.leadingAnchor),
            buttonsStackView.trailingAnchor.constraint(equalTo: textStackView.trailingAnchor),
            buttonsStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
    
    func prepareCoachButton() {
        secondButton.setTitle("Bu Adımı Atla")
        secondButton.setBackgroundColor(.clear)
        secondButton.setTitleColor(DesignKitColorName.contentStrong900.color)
        secondButton.setBorderWidth(1)
        secondButton.setBorderColor(DesignKitColorName.contentStrong900.color)
    }
    
    func prepareClubOfficialButton() {
        secondButton.setTitle("Kulüp Oluştur")
        secondButton.setBackgroundColor(DesignKitColorName.contentStrong900.color)
        secondButton.setTitleColor(.white)
    }
    
    func preparePlayerButton() {
        secondButton.isHidden = true
    }
    
    func hiddenSecondButton(_ isHidden: Bool) {
        secondButton.isHidden = isHidden
    }
    
    func setNavigationBarHidden(_ isHidden: Bool) {
        navigationController?.setNavigationBarHidden(isHidden, animated: true)
    }
}

// MARK: - CKTextFieldDelegate
extension AuthenticationTeamViewController: CKSearchBarDelegate {
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {
        presenter.searching(text)
    }
}

// MARK: - UITableViewDataSource
extension AuthenticationTeamViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return presenter.isEmptyList ? 1 : presenter.filteredList.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if presenter.isEmptyList {
            let cell = EmptyTitleAndButtonTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      title: "Kulübün listede yok mu? Hemen Oluştur.",
                      buttonTitle: "Kulübünü Oluştur",
                      buttonImage: Asset.blackPlus.image,
                      buttonImageSpacing: 4,
                      buttonWidth: 170)
            return cell
        } else {
            let cell = SelectedTeamTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self, model: presenter.filteredList[indexPath.section])
            return cell
        }
    }
}

// MARK: - UITableViewDelegate
extension AuthenticationTeamViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = UIView()
        view.backgroundColor = .clear
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        guard section != 0 else { return 0 }
        return 6
    }
}

// MARK: - SelectedTeamTableViewCellDelegate
extension AuthenticationTeamViewController: SelectedTeamTableViewCellDelegate {
    func selectedTeam(_ model: TeamItemModel) {
        presenter.selectedTeam(model)
    }
}

// MARK: - CKButtonDelegate
extension AuthenticationTeamViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedButton(tag: tag)
    }
}

// MARK: - EmptyTitleAndButtonTableViewCellDelegate
extension AuthenticationTeamViewController: EmptyTitleAndButtonTableViewCellDelegate {
    func didTappedEmptyTitleAndButtonTableViewCell() {
        presenter.openCreateClub()
    }
}
